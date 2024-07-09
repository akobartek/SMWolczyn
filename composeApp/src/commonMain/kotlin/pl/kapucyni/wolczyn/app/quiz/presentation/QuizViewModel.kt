package pl.kapucyni.wolczyn.app.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizAnswer
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizQuestion
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.CheckIfUserIsAllowedUseCase
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.GetQuizUseCase
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.SaveResultUseCase
import pl.kapucyni.wolczyn.app.quiz.presentation.model.QuizScreenAction
import pl.kapucyni.wolczyn.app.quiz.presentation.model.QuizScreenState

class QuizViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    private val checkIfUserIsAllowedUseCase: CheckIfUserIsAllowedUseCase,
    private val saveResultUseCase: SaveResultUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(QuizScreenState())
    val screenState: StateFlow<QuizScreenState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getQuizUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { quiz ->
                        _screenState.update { currentState ->
                            quiz?.let {
                                currentState.copy(isLoading = false, quiz = it)
                            } ?: currentState.copy(isLoading = false, loadingFailed = false)
                        }
                    }
            } catch (_: Exception) {
                _screenState.update { currentState ->
                    currentState.copy(isLoading = false, loadingFailed = false)
                }
            }
        }
    }

    fun handleScreenAction(action: QuizScreenAction) {
        when (action) {
            is QuizScreenAction.UpdateUserId -> onUpdateUserId(action.userId)
            is QuizScreenAction.StartQuizClicked -> onStartQuizClicked()
            is QuizScreenAction.AnswerSelected -> onAnswerSelected(action.question, action.answer)
            is QuizScreenAction.FinishQuizClicked -> onFinishQuiz()
        }
    }

    private fun onUpdateUserId(userId: String) {
        _screenState.update { currentState -> currentState.copy(userId = userId) }
    }

    private fun onStartQuizClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = _screenState.value.userId.takeIf { it.isNotBlank() } ?: return@launch
                val isAllowed = checkIfUserIsAllowedUseCase(userId)
                _screenState.update { currentState ->
                    if (isAllowed) currentState.copy(quizStarted = true)
                    else currentState.copy(userNotAllowed = true)
                }
            } catch (_: Exception) {
                _screenState.update { currentState ->
                    currentState.copy(userNotAllowed = false)
                }
            }
        }
    }

    private fun onAnswerSelected(question: QuizQuestion, answer: QuizAnswer) {
        val updatedQuestion = question.copy(
            answers = question.answers.map { it.copy(isSelected = it == answer) }
        )
        _screenState.update { currentState ->
            currentState.copy(
                quiz = currentState.quiz?.copy(
                    questions = currentState.quiz.questions.map {
                        if (it == question) updatedQuestion else it
                    }
                )
            )
        }
    }

    private fun onFinishQuiz() {
        var result = 0
        _screenState.update { currentState ->
            result = currentState.quiz?.questions?.count { question ->
                question.answers.firstOrNull { it.isCorrect }?.isSelected == true
            } ?: 0
            currentState.copy(
                quizStarted = false,
                quizFinished = true,
                score = result,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            saveResultUseCase(QuizResult(userId = _screenState.value.userId, score = result))
        }
    }
}
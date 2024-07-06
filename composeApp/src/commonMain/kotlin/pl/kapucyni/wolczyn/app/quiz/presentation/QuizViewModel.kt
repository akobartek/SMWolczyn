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
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.CheckIfUserIsAllowedUseCase
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.GetQuizUseCase
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.SaveResultUseCase
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
                        _screenState.update {  currentState ->
                            quiz?.let {
                                currentState.copy(isLoading = false, quiz = it)
                            } ?: currentState.copy(isLoading = false, loadingFailed = false)
                        }
                    }
            } catch (_: Exception) {
                _screenState.update {  currentState ->
                    currentState.copy(isLoading = false, loadingFailed = false)
                }
            }
        }
    }
}
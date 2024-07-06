package pl.kapucyni.wolczyn.app.quiz.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.quiz.data.repository.QuizRepositoryImpl
import pl.kapucyni.wolczyn.app.quiz.data.sources.FirestoreQuizSource
import pl.kapucyni.wolczyn.app.quiz.data.sources.FirestoreQuizType
import pl.kapucyni.wolczyn.app.quiz.domain.repository.QuizRepository
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.CheckIfUserIsAllowedUseCase
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.GetQuizUseCase
import pl.kapucyni.wolczyn.app.quiz.domain.usecases.SaveResultUseCase
import pl.kapucyni.wolczyn.app.quiz.presentation.QuizViewModel

val quizKitchenModule = module {
    single(named(KITCHEN_QUIZ)) {
        FirestoreQuizSource(FirestoreQuizType.KITCHEN)
    }
    single<QuizRepository>(named(KITCHEN_QUIZ)) {
        QuizRepositoryImpl(get(qualifier = named(KITCHEN_QUIZ)))
    }
    single(named(KITCHEN_QUIZ)) {
        GetQuizUseCase(get(qualifier = (named(KITCHEN_QUIZ))))
    }
    single(named(KITCHEN_QUIZ)) {
        CheckIfUserIsAllowedUseCase(get(qualifier = (named(KITCHEN_QUIZ))))
    }
    single(named(KITCHEN_QUIZ)) {
        SaveResultUseCase(get(qualifier = (named(KITCHEN_QUIZ))))
    }
    viewModel(named(KITCHEN_QUIZ)) {
        QuizViewModel(
            get(qualifier = named(KITCHEN_QUIZ)),
            get(qualifier = named(KITCHEN_QUIZ)),
            get(qualifier = named(KITCHEN_QUIZ))
        )
    }
}

const val KITCHEN_QUIZ = "kitchen_quiz"
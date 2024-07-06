package pl.kapucyni.wolczyn.app.kitchen.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.kitchen.data.repository.KitchenRepositoryImpl
import pl.kapucyni.wolczyn.app.kitchen.data.sources.BasicKitchenSource
import pl.kapucyni.wolczyn.app.kitchen.data.sources.FirestoreKitchenSource
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository
import pl.kapucyni.wolczyn.app.kitchen.domain.usecases.GetKitchenMenuUseCase
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenViewModel
import pl.kapucyni.wolczyn.app.quiz.data.sources.FirestoreQuizSource
import pl.kapucyni.wolczyn.app.quiz.data.sources.FirestoreQuizType

val kitchenModule = module {
    single { FirestoreKitchenSource() }
    single { BasicKitchenSource() }
    single(named(KITCHEN_QUIZ_SOURCE)) {
        FirestoreQuizSource(FirestoreQuizType.KITCHEN)
    }

    single<KitchenRepository> {
        KitchenRepositoryImpl(get(), get(), get(qualifier = named(KITCHEN_QUIZ_SOURCE)))
    }
    single { GetKitchenMenuUseCase(get()) }

    viewModel { KitchenViewModel(get()) }
}

const val KITCHEN_QUIZ_SOURCE = "kitchen_quiz"
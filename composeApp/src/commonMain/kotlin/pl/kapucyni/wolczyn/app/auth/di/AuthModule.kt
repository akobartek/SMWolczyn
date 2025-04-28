package pl.kapucyni.wolczyn.app.auth.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.auth.data.FirebaseAuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.usecase.SignUpUseCase
import pl.kapucyni.wolczyn.app.auth.domain.usecase.UpdateUserUseCase
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileViewModel
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInViewModel
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpViewModel

val authModule = module {
    single<AuthRepository> { FirebaseAuthRepository(get(), get()) }
    factory { SignUpUseCase(get()) }
    factory { UpdateUserUseCase(get()) }

    viewModel { (email: String) -> SignInViewModel(email, get()) }
    viewModel { (email: String) -> SignUpViewModel(email, get()) }
    viewModel { (user: User) -> EditProfileViewModel(user, get()) }
}
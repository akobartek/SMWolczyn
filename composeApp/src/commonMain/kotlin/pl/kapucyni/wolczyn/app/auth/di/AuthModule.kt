package pl.kapucyni.wolczyn.app.auth.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.auth.data.FirebaseAuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.usecase.SignUpUseCase
import pl.kapucyni.wolczyn.app.auth.domain.usecase.UpdateUserUseCase
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileViewModel
import pl.kapucyni.wolczyn.app.auth.presentation.manager.AccountManagerViewModel
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInViewModel
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpViewModel

val authModule = module {
    single<AuthRepository> { FirebaseAuthRepository(get(), get()) }
    factory { SignUpUseCase(get()) }
    factory { UpdateUserUseCase(get()) }

    viewModelOf(::EditProfileViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::AccountManagerViewModel)
}
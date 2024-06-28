package pl.kapucyni.wolczyn.app.common.di

import org.koin.core.module.Module
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.common.utils.dataStore

actual val platformModule: Module = module {
    single { dataStore() }
}
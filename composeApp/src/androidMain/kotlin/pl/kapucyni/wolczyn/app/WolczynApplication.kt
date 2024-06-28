package pl.kapucyni.wolczyn.app

import android.app.Application
import android.content.Context
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.common.utils.initKoin

class WolczynApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            module {
                single<Context> { this@WolczynApplication }
            }
        )
    }
}
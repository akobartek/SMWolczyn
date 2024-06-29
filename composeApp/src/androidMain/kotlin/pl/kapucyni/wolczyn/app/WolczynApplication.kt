package pl.kapucyni.wolczyn.app

import android.app.Application
import android.content.Context
import pl.kapucyni.wolczyn.app.common.utils.initKoinAndroid
import org.koin.dsl.module

class WolczynApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(
            module {
                single<Context> { this@WolczynApplication }
            }
        )
    }
}
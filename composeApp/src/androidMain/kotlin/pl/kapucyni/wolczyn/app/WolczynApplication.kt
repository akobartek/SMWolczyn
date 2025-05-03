package pl.kapucyni.wolczyn.app

import android.app.Activity
import android.app.Application
import pl.kapucyni.wolczyn.app.common.utils.initKoin
import org.koin.android.ext.koin.androidContext

class WolczynApplication : Application() {

    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@WolczynApplication)
        }
    }
}
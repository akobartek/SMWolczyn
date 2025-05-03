package pl.kapucyni.wolczyn.app.common.presentation

import android.app.Application
import pl.kapucyni.wolczyn.app.WolczynApplication
import kotlin.system.exitProcess

class AndroidLifecycleManager(val app: Application) : LifecycleManager {

    override fun closeApp() {
        (app as WolczynApplication).currentActivity
            ?.finish()
            ?: exitProcess(0)
    }
}
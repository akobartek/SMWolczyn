package pl.kapucyni.wolczyn.app.common.presentation

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.objc.sel_registerName
import platform.posix.exit

@OptIn(ExperimentalForeignApi::class)
class AppleLifecycleManager: LifecycleManager {

    override fun closeApp() {
        UIApplication.sharedApplication
            .performSelector(sel_registerName("suspend"))
            ?: exit(0)
    }
}
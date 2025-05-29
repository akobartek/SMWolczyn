package pl.kapucyni.wolczyn.app.common.utils

class NativeCodeScanner : CodeScanner {

    override val available: Boolean
        get() = false

    override fun startScanning(onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        // not available yet
    }
}
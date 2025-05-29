package pl.kapucyni.wolczyn.app.common.utils

interface CodeScanner {
    val available: Boolean
    fun startScanning(onSuccess: (String) -> Unit, onFailure: () -> Unit)
}
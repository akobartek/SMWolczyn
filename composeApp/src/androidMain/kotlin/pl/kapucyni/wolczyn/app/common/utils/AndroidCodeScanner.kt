package pl.kapucyni.wolczyn.app.common.utils

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class AndroidCodeScanner(private val context: Context) : CodeScanner {

    override val available: Boolean
        get() = true

    override fun startScanning(onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()
        GmsBarcodeScanning
            .getClient(context, options)
            .startScan()
            .addOnSuccessListener { barcode ->
                barcode.rawValue
                    ?.takeIf { it.isValidEmail() }
                    ?.let {
                        onSuccess(it)
                        return@addOnSuccessListener
                    }
                onFailure()
            }
            .addOnCanceledListener {}
            .addOnFailureListener { onFailure() }
    }
}
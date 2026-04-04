package pl.kapucyni.wolczyn.app.common.utils

import android.content.Context
import android.content.pm.PackageManager
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class AndroidCodeScanner(private val context: Context) : CodeScanner {

    override val available: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    override fun startScanning(
        onSuccess: (String) -> Unit,
        onFailure: (invalidValue: Boolean) -> Unit,
        onCancel: () -> Unit,
    ) {
        if (!available) {
            onFailure(false)
            return
        }

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
                    } ?: run { onFailure(true) }
            }
            .addOnCanceledListener { onCancel() }
            .addOnFailureListener { exc ->
                if (exc is MlKitException) {
                    when (exc.errorCode) {
                        MlKitException.CODE_SCANNER_CANCELLED,
                        MlKitException.CANCELLED,
                        MlKitException.INTERNAL,
                             -> onCancel()

                        else -> onFailure(false)
                    }
                } else {
                    onFailure(false)
                }
            }
    }
}
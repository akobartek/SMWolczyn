package pl.kapucyni.wolczyn.app.common.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import pl.kapucyni.wolczyn.app.core.domain.repository.LogRepository

class AndroidCodeScanner(
    private val context: Context,
    private val logRepository: LogRepository,
) : CodeScanner {

    override val available: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    override fun startScanning(
        onSuccess: (String) -> Unit,
        onFailure: (invalidValue: Boolean) -> Unit,
        onCancel: () -> Unit,
        onNoScannerFound: () -> Unit,
    ) {
        if (!available) {
            onNoScannerFound()
            return
        }

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()
        try {
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
                            MlKitException.CODE_SCANNER_UNAVAILABLE,
                            MlKitException.CODE_SCANNER_GOOGLE_PLAY_SERVICES_VERSION_TOO_OLD,
                                 -> onNoScannerFound()

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
        } catch (exc: ActivityNotFoundException) {
            logRepository.logException("Nie znaleziono skanera", exc)
            onNoScannerFound()
        } catch (exc: Exception) {
            logRepository.logException("Błąd uruchamiania skanera", exc)
            onFailure(false)
        }
    }
}
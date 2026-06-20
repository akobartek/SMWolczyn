package pl.kapucyni.wolczyn.app.common.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class NativeCodeScanner : CodeScanner {

    override val available: Boolean
        get() = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) != null

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

        checkAndRequestCameraPermission { isGranted ->
            dispatch_async(dispatch_get_main_queue()) {
                if (isGranted) {
                    showScanner(onSuccess, onFailure, onCancel)
                } else {
                    onFailure(false)
                }
            }
        }
    }

    private fun showScanner(
        onSuccess: (String) -> Unit,
        onFailure: (invalidValue: Boolean) -> Unit,
        onCancel: () -> Unit,
    ) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

        val scannerViewController = ScannerViewController(
            onSuccess = { code ->
                if (code.isValidEmail()) onSuccess(code)
                else onFailure(true)
            },
            onCancel = { onCancel() },
        )

        scannerViewController.modalPresentationStyle = UIModalPresentationFullScreen
        rootViewController?.presentViewController(scannerViewController, animated = true, completion = null)
    }

    private fun checkAndRequestCameraPermission(onResult: (Boolean) -> Unit) {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)

        when (status) {
            AVAuthorizationStatusAuthorized -> onResult(true)
            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    onResult(granted)
                }
            }
            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> onResult(false)
            else -> onResult(false)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class ScannerViewController(
    private val onSuccess: (String) -> Unit,
    private val onCancel: () -> Unit,
) : UIViewController(null, null), AVCaptureMetadataOutputObjectsDelegateProtocol {

    private var captureSession: AVCaptureSession? = null
    private var previewLayer: AVCaptureVideoPreviewLayer? = null

    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.blackColor

        setupCamera()
        setupCloseButton()
    }

    private fun setupCamera() {
        val session = AVCaptureSession()
        captureSession = session

        if (session.canSetSessionPreset(AVCaptureSessionPresetHigh)) {
            session.sessionPreset = AVCaptureSessionPresetHigh
        }

        val videoCaptureDevice = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        videoCaptureDevice?.let { device ->
            if (device.lockForConfiguration(null)) {
                if (device.isFocusModeSupported(AVCaptureFocusModeContinuousAutoFocus)) {
                    device.focusMode = AVCaptureFocusModeContinuousAutoFocus
                }

                if (device.isExposureModeSupported(AVCaptureExposureModeContinuousAutoExposure)) {
                    device.exposureMode = AVCaptureExposureModeContinuousAutoExposure
                }

                device.unlockForConfiguration()
            }
        }
        val videoInput = videoCaptureDevice?.let {
            AVCaptureDeviceInput.deviceInputWithDevice(it, null)
        }

        if (videoInput != null && session.canAddInput(videoInput)) {
            session.addInput(videoInput)
        } else {
            closeScanner()
            return
        }

        val metadataOutput = AVCaptureMetadataOutput()
        if (session.canAddOutput(metadataOutput)) {
            session.addOutput(metadataOutput)
            metadataOutput.setMetadataObjectsDelegate(this, dispatch_get_main_queue())
            metadataOutput.setMetadataObjectTypes(listOf(AVMetadataObjectTypeQRCode))
        } else {
            closeScanner()
            return
        }

        val layer = AVCaptureVideoPreviewLayer.layerWithSession(session)
        layer.videoGravity = AVLayerVideoGravityResizeAspectFill
        view.layer.addSublayer(layer)
        previewLayer = layer

        session.startRunning()
    }

    private fun setupCloseButton() {
        val closeAction = UIAction.actionWithHandler { _ ->
            closeScanner()
        }

        val closeButton = UIButton.buttonWithType(UIButtonTypeSystem).apply {
            val iconConfig = UIImageSymbolConfiguration.configurationWithPointSize(16.0, UIImageSymbolWeightBold)
            val closeIcon = UIImage.systemImageNamed("xmark", iconConfig)

            setImage(closeIcon, forState = UIControlStateNormal)
            tintColor = UIColor.whiteColor

            backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.6)
            layer.cornerRadius = 20.0
            addAction(closeAction, forControlEvents = UIControlEventTouchUpInside)
            translatesAutoresizingMaskIntoConstraints = false
        }

        view.addSubview(closeButton)

        NSLayoutConstraint.activateConstraints(listOf(
            closeButton.topAnchor.constraintEqualToAnchor(view.safeAreaLayoutGuide.topAnchor, constant = 16.0),
            closeButton.trailingAnchor.constraintEqualToAnchor(view.safeAreaLayoutGuide.trailingAnchor, constant = -16.0),
            closeButton.widthAnchor.constraintEqualToConstant(40.0),
            closeButton.heightAnchor.constraintEqualToConstant(40.0)
        ))
    }

    private fun closeScanner() {
        captureSession?.stopRunning()
        dismissViewControllerAnimated(true) {
            onCancel()
        }
    }

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection
    ) {
        val metadataObject = didOutputMetadataObjects.firstOrNull() as? AVMetadataMachineReadableCodeObject
        if (metadataObject?.type == AVMetadataObjectTypeQRCode) {
            val stringValue = metadataObject?.stringValue
            if (stringValue != null) {
                captureSession?.stopRunning()
                dismissViewControllerAnimated(true) {
                    onSuccess(stringValue)
                }
            }
        }
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        previewLayer?.frame = view.layer.bounds
    }
}
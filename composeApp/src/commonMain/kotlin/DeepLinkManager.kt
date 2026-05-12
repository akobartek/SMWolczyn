import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("DeepLinkManager")
object DeepLinkManager {

    private val _resetCode = MutableStateFlow<String?>(null)
    val resetCode = _resetCode.asStateFlow()

    private val _verificationCode = MutableStateFlow<String?>(null)
    val verificationCode = _verificationCode.asStateFlow()

    fun onLinkReceived(link: String) {
        if (link.contains("/reset-password") || link.contains("oobCode=")) {
            try {
                val url = Url(urlString = link)
                val mode = url.parameters["mode"]
                val oobCode = url.parameters["oobCode"] ?: return

                when (mode) {
                    "verifyEmail" -> _verificationCode.value = oobCode
                    "resetPassword" -> _resetCode.value = oobCode
                }
            } catch (_: Exception) {
                println("Error occurred while parsing deep link!")
            }
        }
    }

    fun clearResetCode() {
        _resetCode.value = null
    }

    fun clearVerificationCode() {
        _verificationCode.value = null
    }
}
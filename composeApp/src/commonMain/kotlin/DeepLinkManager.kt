import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("DeepLinkManager")
object DeepLinkManager {
    private val _resetCode = MutableStateFlow<String?>(null)
    val resetCode = _resetCode.asStateFlow()

    fun onUrlReceived(url: String) {
        if (url.contains("/reset-password")|| url.contains("oobCode=")) {
            val code = url.substringAfter("oobCode=", "").substringBefore("&")
            if (code.isNotEmpty()) {
                _resetCode.value = code
            }
        }
    }

    fun clearCode() {
        _resetCode.value = null
    }
}
@file:Suppress("CAST_NEVER_SUCCEEDS")

package pl.kapucyni.wolczyn.app.common.utils

import platform.Foundation.NSMutableCharacterSet
import platform.Foundation.NSString
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters

actual fun encodeUrl(value: String): String {
    val allowedChars = NSMutableCharacterSet.alphanumericCharacterSet().apply {
        addCharactersInString("-._~")
    }
    val nsString = value as NSString
    return nsString.stringByAddingPercentEncodingWithAllowedCharacters(allowedChars) ?: value
}
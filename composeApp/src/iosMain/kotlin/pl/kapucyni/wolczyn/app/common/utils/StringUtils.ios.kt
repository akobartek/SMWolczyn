package pl.kapucyni.wolczyn.app.common.utils

import platform.Foundation.NSString
import platform.Foundation.decomposedStringWithCompatibilityMapping

actual fun String.normalizeMultiplatform(): String {
    val regex = Regex("[^A-Za-z0-9 ]")
    @Suppress("CAST_NEVER_SUCCEEDS")
    val str = this as NSString
    return str.decomposedStringWithCompatibilityMapping
        .replace("\n", " ")
        .replace(regex, "")
        .lowercase()
}
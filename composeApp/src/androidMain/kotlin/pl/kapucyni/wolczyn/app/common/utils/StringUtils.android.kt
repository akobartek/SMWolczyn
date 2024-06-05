package pl.kapucyni.wolczyn.app.common.utils

import java.text.Normalizer

actual fun String.normalizeMultiplatform(): String {
    val regex = Regex("[^A-Za-z0-9 ]")
    return Normalizer.normalize(this, Normalizer.Form.NFKD)
        .replace("\n", " ")
        .replace(regex, "")
        .lowercase()
}
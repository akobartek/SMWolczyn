package pl.kapucyni.wolczyn.app.common.utils

import android.net.Uri

actual fun encodeUrl(value: String): String =
    Uri.encode(value)
package pl.kapucyni.wolczyn.app.common.utils

expect fun String.normalizeMultiplatform(): String

expect fun randomUUID(): String

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    return this.matches(emailRegex)
}
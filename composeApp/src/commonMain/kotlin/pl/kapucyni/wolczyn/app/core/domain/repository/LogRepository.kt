package pl.kapucyni.wolczyn.app.core.domain.repository

interface LogRepository {
    fun setUserId(userId: String)
    fun setCustomKey(key: String, value: String)
    fun setCustomKey(key: String, value: Boolean)
    fun log(message: String)
    fun logException(message: String? = null, exc: Throwable)
}
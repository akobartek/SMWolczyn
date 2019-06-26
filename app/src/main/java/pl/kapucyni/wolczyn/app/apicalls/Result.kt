package pl.kapucyni.wolczyn.app.apicalls

sealed class Result<out T : Any?> {
    data class Success<out T : Any?>(val data: T?) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
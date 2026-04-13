package pl.kapucyni.wolczyn.app.core.data.repository

import dev.gitlive.firebase.crashlytics.FirebaseCrashlytics
import pl.kapucyni.wolczyn.app.core.domain.repository.LogRepository

class CrashlyticsLogRepository(
    private val crashlytics: FirebaseCrashlytics,
) : LogRepository {

    override fun setUserId(userId: String) =
        crashlytics.setUserId(userId)

    override fun setCustomKey(key: String, value: String) =
        crashlytics.setCustomKey(key, value)

    override fun setCustomKey(key: String, value: Boolean) =
        crashlytics.setCustomKey(key, value)

    override fun log(message: String) =
        crashlytics.log(message)

    override fun logException(message: String?, exc: Throwable) {
        message?.let { log(it) }
        crashlytics.recordException(exc)
    }
}
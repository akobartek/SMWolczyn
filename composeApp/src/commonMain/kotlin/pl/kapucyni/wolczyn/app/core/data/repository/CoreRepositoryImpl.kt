package pl.kapucyni.wolczyn.app.core.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.HomeNotification
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class CoreRepositoryImpl(
    private val firestoreSource: FirestoreHomeSource,
) : CoreRepository {

    override val appConfiguration: StateFlow<AppConfiguration> =
        firestoreSource.getAppConfiguration()
            .map { it ?: AppConfiguration() }
            .catch { emit(AppConfiguration()) }
            .stateIn(
                scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
                started = SharingStarted.Eagerly,
                initialValue = AppConfiguration(),
            )

    override fun getHomeNotifications(): Flow<List<HomeNotification>> =
        firestoreSource.getHomeNotifications()
}
package pl.kapucyni.wolczyn.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.HomeNotification
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class CoreRepositoryImpl(
    private val firestoreSource: FirestoreHomeSource,
) : CoreRepository {

    override fun getHomeNotifications(): Flow<List<HomeNotification>> =
        firestoreSource.getHomeNotifications()

    override fun getAppConfiguration(): Flow<AppConfiguration?> =
        firestoreSource.getAppConfiguration()
}
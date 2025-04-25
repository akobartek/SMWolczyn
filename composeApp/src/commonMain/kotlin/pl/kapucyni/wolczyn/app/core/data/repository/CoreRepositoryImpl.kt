package pl.kapucyni.wolczyn.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class CoreRepositoryImpl(
    private val firestoreSource: FirestoreHomeSource,
) : CoreRepository {

    override fun getAppState(): Flow<AppState> =
        combine(
            firestoreSource.getAppConfiguration(),
            firestoreSource.getHomeNotifications(),
        ) { configuration, notifications ->
            AppState.init(configuration, notifications)
        }
}
package pl.kapucyni.wolczyn.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.HomeNotification

interface CoreRepository {
    val appConfiguration: StateFlow<AppConfiguration>
    fun getHomeNotifications(): Flow<List<HomeNotification>>
}
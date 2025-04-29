package pl.kapucyni.wolczyn.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.HomeNotification

interface CoreRepository {
    fun getHomeNotifications(): Flow<List<HomeNotification>>
    fun getAppConfiguration(): Flow<AppConfiguration?>
}
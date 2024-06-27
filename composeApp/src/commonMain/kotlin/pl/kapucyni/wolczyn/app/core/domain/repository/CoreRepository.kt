package pl.kapucyni.wolczyn.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.core.domain.model.AppState

interface CoreRepository {
    fun getAppState(): Flow<AppState>
}
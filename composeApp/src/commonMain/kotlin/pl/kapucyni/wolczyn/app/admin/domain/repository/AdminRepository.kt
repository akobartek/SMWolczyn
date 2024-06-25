package pl.kapucyni.wolczyn.app.admin.domain.repository

import kotlinx.coroutines.flow.Flow

interface AdminRepository<out T> {
    fun getAppData(): Flow<T>
}
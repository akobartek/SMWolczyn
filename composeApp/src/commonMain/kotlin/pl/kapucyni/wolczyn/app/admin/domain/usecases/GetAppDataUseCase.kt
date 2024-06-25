package pl.kapucyni.wolczyn.app.admin.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository

class GetAppDataUseCase<T>(private val adminRepository: AdminRepository<T>) {
    operator fun invoke(): Flow<T> = adminRepository.getAppData()
}
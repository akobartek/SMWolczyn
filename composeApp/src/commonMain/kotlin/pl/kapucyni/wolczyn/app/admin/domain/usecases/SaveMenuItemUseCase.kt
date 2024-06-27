package pl.kapucyni.wolczyn.app.admin.domain.usecases

import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem

class SaveMenuItemUseCase(private val adminRepository: AdminRepository<FirestoreData>) {
    suspend operator fun invoke(item: FirestoreMenuItem) = adminRepository.saveMenuItem(item)
}
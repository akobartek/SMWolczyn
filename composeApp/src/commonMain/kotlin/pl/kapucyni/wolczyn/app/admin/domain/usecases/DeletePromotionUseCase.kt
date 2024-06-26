package pl.kapucyni.wolczyn.app.admin.domain.usecases

import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository

class DeletePromotionUseCase(private val adminRepository: AdminRepository<FirestoreData>) {
    suspend operator fun invoke(promoId: String, isFromKitchen: Boolean) =
        adminRepository.deletePromotion(promoId, isFromKitchen)
}
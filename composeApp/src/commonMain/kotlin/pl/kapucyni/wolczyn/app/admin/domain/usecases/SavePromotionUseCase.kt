package pl.kapucyni.wolczyn.app.admin.domain.usecases

import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion

class SavePromotionUseCase(private val adminRepository: AdminRepository<FirestoreData>) {
    suspend operator fun invoke(promotion: FirestorePromotion, isFromKitchen: Boolean) =
        adminRepository.savePromotion(promotion, isFromKitchen)
}
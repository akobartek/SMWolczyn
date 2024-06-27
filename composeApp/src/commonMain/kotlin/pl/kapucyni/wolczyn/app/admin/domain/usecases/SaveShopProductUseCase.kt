package pl.kapucyni.wolczyn.app.admin.domain.usecases

import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

class SaveShopProductUseCase(private val adminRepository: AdminRepository<FirestoreData>) {
    suspend operator fun invoke(product: FirestoreShopProduct) =
        adminRepository.saveShopProduct(product)
}
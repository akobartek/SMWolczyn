package pl.kapucyni.wolczyn.app.admin.presentation

import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion

sealed class AdminScreenAction {
    data class AddPromotion(val name: String, val isFromKitchen: Boolean) : AdminScreenAction()
    data class UpdatePromotion(val promotion: FirestorePromotion, val isFromKitchen: Boolean) :
        AdminScreenAction()

    data class DeletePromotion(val id: String, val isFromKitchen: Boolean) : AdminScreenAction()
}
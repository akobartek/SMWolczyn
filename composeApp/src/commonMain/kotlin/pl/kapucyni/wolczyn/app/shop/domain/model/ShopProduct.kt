package pl.kapucyni.wolczyn.app.shop.domain.model

data class ShopProduct(
    val id: String = "",
    val name: String = "",
    val photoUrls: Map<ProductColor, List<String>> = mapOf(),
    val sizes: String = "",
) {
    fun getMainPhotoUrl() =
        photoUrls[ProductColor.NONE]?.let {
            if (it.isNotEmpty()) it.first() else getAnyPhoto()
        } ?: getAnyPhoto()

    private fun getAnyPhoto() = photoUrls.values.flatten().firstOrNull()
}
package pl.kapucyni.wolczyn.app.common.presentation.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.json.Json
import pl.kapucyni.wolczyn.app.common.utils.encodeUrl
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct

internal val ShopProductParameterType = object: NavType<ShopProduct>(isNullableAllowed = false) {

    override fun put(bundle: SavedState, key: String, value: ShopProduct) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun get(bundle: SavedState, key: String): ShopProduct? =
        bundle.read { getStringOrNull(key) }?.let { parseValue(it) }

    override fun parseValue(value: String): ShopProduct =
        Json.decodeFromString(value)

    override fun serializeAsValue(value: ShopProduct): String {
        val json = Json.encodeToString(value)
        return encodeUrl(json)
    }
}
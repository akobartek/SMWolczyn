package pl.kapucyni.wolczyn.app.common.presentation

sealed class Screen(val route: String) {
    data object Home: Screen(HOME_ROUTE)
    data object Schedule: Screen(SCHEDULE_ROUTE)
    data object SongBook: Screen(SONG_BOOK_ROUTE)
    data object Kitchen: Screen(KITCHEN_ROUTE)
    data object Shop: Screen(SHOP_ROUTE)
    data object ShopProduct: Screen(SHOP_PRODUCT_ROUTE) {
        fun productRoute(id: String) = "shop_product/$id"
    }
    data object Decalogue: Screen(DECALOGUE_ROUTE)
    data object Breviary: Screen(BREVIARY_ROUTE)
    data object Archive: Screen(ARCHIVE_ROUTE)

    companion object {
        const val HOME_ROUTE = "home"
        const val SCHEDULE_ROUTE = "schedule"
        const val SONG_BOOK_ROUTE = "song_book"
        const val KITCHEN_ROUTE = "kitchen"
        const val SHOP_ROUTE = "shop"
        const val SHOP_PRODUCT_ROUTE = "shop_product/{productId}"
        const val DECALOGUE_ROUTE = "decalogue"
        const val WEATHER_ROUTE = "weather"
        const val BREVIARY_ROUTE = "breviary"
        const val ARCHIVE_ROUTE = "archive"
    }
}
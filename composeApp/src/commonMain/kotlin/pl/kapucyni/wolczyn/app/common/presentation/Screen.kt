package pl.kapucyni.wolczyn.app.common.presentation

sealed class Screen(val route: String) {
    data object Home : Screen(HOME_ROUTE)
    data object Admin : Screen(ADMIN_ROUTE)
    data object Schedule : Screen(SCHEDULE_ROUTE)
    data object SongBook : Screen(SONG_BOOK_ROUTE)
    data object Kitchen : Screen(KITCHEN_ROUTE)
    data object Decalogue : Screen(DECALOGUE_ROUTE)

    data object Shop : Screen(SHOP_ROUTE)
    data object ShopProduct : Screen(SHOP_PRODUCT_ROUTE) {
        fun productRoute(id: String) = SHOP_PRODUCT_ROUTE.replace("{$ARGUMENT_PRODUCT_ID}", id)
    }

    data object BreviarySelect : Screen(BREVIARY_SELECT_ROUTE)
    data object BreviaryText : Screen(BREVIARY_TEXT_ROUTE) {
        fun breviaryTextRoute(position: Int, date: String) =
            BREVIARY_TEXT_ROUTE
                .replace("{$ARGUMENT_BREVIARY_POSITION}", position.toString())
                .replace("{$ARGUMENT_BREVIARY_DATE}", date)
    }

    data object BreviarySave : Screen(BREVIARY_SAVE_ROUTE) {
        fun breviarySaveRoute(date: String) =
            BREVIARY_SAVE_ROUTE.replace("{$ARGUMENT_BREVIARY_DATE}", date)
    }

    data object Archive : Screen(ARCHIVE_ROUTE)
    data object ArchiveMeeting : Screen(ARCHIVE_MEETING_ROUTE) {
        fun meetingRoute(number: Int) =
            ARCHIVE_MEETING_ROUTE.replace("{$ARGUMENT_MEETING_NUMBER}", number.toString())
    }

    data object Quiz : Screen(QUIZ_ROUTE) {
        fun quizRoute(type: String) = QUIZ_ROUTE.replace("{$ARGUMENT_QUIZ_TYPE}", type)
    }

    companion object {
        const val ARGUMENT_PRODUCT_ID = "productId"
        const val ARGUMENT_MEETING_NUMBER = "meetingNumber"
        const val ARGUMENT_BREVIARY_DATE = "breviaryDate"
        const val ARGUMENT_BREVIARY_POSITION = "breviaryPosition"
        const val ARGUMENT_QUIZ_TYPE = "quizType"

        const val HOME_ROUTE = "home"
        const val ADMIN_ROUTE = "admin"
        const val SCHEDULE_ROUTE = "schedule"
        const val SONG_BOOK_ROUTE = "song_book"
        const val KITCHEN_ROUTE = "kitchen"
        const val SHOP_ROUTE = "shop"
        const val SHOP_PRODUCT_ROUTE = "shop_product/{$ARGUMENT_PRODUCT_ID}"
        const val DECALOGUE_ROUTE = "decalogue"
        const val BREVIARY_SELECT_ROUTE = "breviary_select"
        const val BREVIARY_TEXT_ROUTE =
            "breviary_text/{$ARGUMENT_BREVIARY_POSITION}/{$ARGUMENT_BREVIARY_DATE}"
        const val BREVIARY_SAVE_ROUTE = "breviary_save/{$ARGUMENT_BREVIARY_DATE}"
        const val ARCHIVE_ROUTE = "archive"
        const val ARCHIVE_MEETING_ROUTE = "archive_meeting/{$ARGUMENT_MEETING_NUMBER}"
        const val QUIZ_ROUTE = "quiz/{$ARGUMENT_QUIZ_TYPE}"
    }
}
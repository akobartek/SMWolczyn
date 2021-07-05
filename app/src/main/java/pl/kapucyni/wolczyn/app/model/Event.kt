package pl.kapucyni.wolczyn.app.model

data class Event(
    val id: String,
    val day: String,
    val date: String,
    val hour: String,
    val name: String,
    val eventPlace: EventPlace,
    val eventType: EventType,
    val guestIndex: Int?,
    var videoUrl: String? = ""
)

data class EventFirestore(
    val id: String = "",
    val videoUrl: String = ""
)

enum class EventType {
    CONFERENCE, CONCERT, MASS, DEVOTION, EXTRA, ORGANIZATION, GROUPS, MEAL, BREVIARY, PRAYER, OTHER
}

enum class EventPlace {
    AMPHITHEATRE, WHITE_TENT, GARDEN, CAMPSITE, COURT, EVERYWHERE, CHURCH
}
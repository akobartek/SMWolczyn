package pl.kapucyni.wolczyn.app.model

data class User(
    val type: Int?,
    val state: Int?,
    val status: Int?,
    val group: Int?,
    val name: String?,
    val surname: String?,
    val photo_url: String?,
    val number: Int?,
    val bears: Int?
)
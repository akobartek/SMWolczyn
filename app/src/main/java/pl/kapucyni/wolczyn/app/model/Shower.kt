package pl.kapucyni.wolczyn.app.model

data class Shower(
    val id: Int?,
    val hour: String?,
    val count: ShowerCount?
)

data class ShowerCount(
    val K: Int?,
    val M: Int?
)
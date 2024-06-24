package pl.kapucyni.wolczyn.app.breviary.domain.model

data class Psalmody(
    val breviaryPages: String = "",
    val psalms: List<Psalm> = listOf()
)

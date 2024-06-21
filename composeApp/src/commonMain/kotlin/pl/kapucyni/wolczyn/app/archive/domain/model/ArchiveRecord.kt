package pl.kapucyni.wolczyn.app.archive.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArchiveRecord(
    val name: String = "",
    val videoUrl: String = ""
)

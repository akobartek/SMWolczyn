package pl.kapucyni.wolczyn.app.archive.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArchiveMeeting(
    val name: String = "",
    val photoUrl: String = "",
    val number: Int = 0,
    val anthem: String = "",
    val records: List<ArchiveRecord> = listOf()
)

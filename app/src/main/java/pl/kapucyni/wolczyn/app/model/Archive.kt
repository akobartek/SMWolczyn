package pl.kapucyni.wolczyn.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ArchiveMeeting(
    val name: String = "",
    val photoUrl: String = "",
    val number: Int = 0,
    val anthem: String = "",
    val records: ArrayList<Record> = arrayListOf()
)

@Parcelize
data class Record(
    val name: String = "",
    val videoUrl: String = ""
) : Parcelable
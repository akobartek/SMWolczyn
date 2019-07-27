package pl.kapucyni.wolczyn.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ArchiveMeeting(
    val name: String = "",
    val photoUrl: String = "",
    val number: Int = 0,
    val records: ArrayList<Record> = arrayListOf()
)

@Parcelize
data class Record(
    val name: String = "",
    val videoUrl: String = ""
) : Parcelable
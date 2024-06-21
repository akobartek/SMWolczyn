package pl.kapucyni.wolczyn.app.archive.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection

private const val ARCHIVE_COLLECTION = "archive"

internal fun getFirestoreArchive(): Flow<List<ArchiveMeeting>> =
    Firebase.firestore.getFirestoreCollection(ARCHIVE_COLLECTION)
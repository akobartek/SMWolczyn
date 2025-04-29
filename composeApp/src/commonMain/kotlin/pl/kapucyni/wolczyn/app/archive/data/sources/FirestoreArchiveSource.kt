package pl.kapucyni.wolczyn.app.archive.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionFlow
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionByField

class FirestoreArchiveSource {
    companion object {
        private const val ARCHIVE_COLLECTION = "archive"
        private const val ARCHIVE_NUMBER_FIELD = "number"
    }

    fun getArchive(): Flow<List<ArchiveMeeting>> =
        Firebase.firestore.getFirestoreCollectionFlow(ARCHIVE_COLLECTION)

    fun getArchiveMeetingByNumber(number: Int): Flow<ArchiveMeeting?> =
        Firebase.firestore.getFirestoreCollectionByField(
            collectionName = ARCHIVE_COLLECTION,
            fieldName = ARCHIVE_NUMBER_FIELD,
            fieldValue = number
        )
}
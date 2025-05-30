package pl.kapucyni.wolczyn.app.core.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionFlow
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreDocument
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.HomeNotification

class FirestoreHomeSource {
    companion object {
        private const val HOME_COLLECTION = "home"
        private const val CONFIGURATION_COLLECTION = "configuration"
    }

    fun getHomeNotifications(): Flow<List<HomeNotification>> =
        Firebase.firestore.getFirestoreCollectionFlow(HOME_COLLECTION)

    fun getAppConfiguration(): Flow<AppConfiguration?> =
        Firebase.firestore.getFirestoreDocument(
            collectionName = CONFIGURATION_COLLECTION,
            documentId = CONFIGURATION_COLLECTION,
        )
}
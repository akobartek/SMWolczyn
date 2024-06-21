package pl.kapucyni.wolczyn.app.common.utils

import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T> FirebaseFirestore.getFirestoreCollection(collectionName: String): Flow<List<T>> =
    this.collection(collectionName)
        .snapshots
        .map { querySnapshot ->
            querySnapshot.documents.map { it.data() }
        }

fun FirebaseFirestore.getFirestoreCollectionCount(collectionName: String): Flow<Int> =
    this.collection(collectionName)
        .snapshots
        .map { it.documents.size }
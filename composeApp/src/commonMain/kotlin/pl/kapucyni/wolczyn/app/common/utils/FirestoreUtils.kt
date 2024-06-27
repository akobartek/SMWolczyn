package pl.kapucyni.wolczyn.app.common.utils

import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T> FirebaseFirestore.getFirestoreCollection(collectionName: String): Flow<List<T>> =
    this.collection(collectionName)
        .snapshots
        .map { querySnapshot ->
            querySnapshot.documents.map { it.data() }
        }

inline fun <reified T> FirebaseFirestore.getFirestoreCollectionByField(
    collectionName: String,
    fieldName: String,
    fieldValue: Any
): Flow<T?> =
    this.collection(collectionName)
        .where { fieldName equalTo fieldValue }
        .snapshots
        .map { querySnapshot ->
            querySnapshot.documents
                .map<DocumentSnapshot, T> { it.data() }
                .firstOrNull()
        }

inline fun <reified T> FirebaseFirestore.getFirestoreDocument(
    collectionName: String,
    documentId: String,
): Flow<T> =
    this.collection(collectionName)
        .document(documentId)
        .snapshots
        .map { it.data() }

suspend inline fun <reified T> FirebaseFirestore.saveObject(
    collectionName: String,
    id: String,
    data: T
) =
    this.collection(collectionName)
        .document(id)
        .set(data)

suspend inline fun FirebaseFirestore.deleteObject(
    collectionName: String,
    id: String,
) =
    this.collection(collectionName)
        .document(id)
        .delete()
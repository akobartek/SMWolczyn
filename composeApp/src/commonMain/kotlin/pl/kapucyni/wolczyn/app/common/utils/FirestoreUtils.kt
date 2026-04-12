package pl.kapucyni.wolczyn.app.common.utils

import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

inline fun <reified T> FirebaseFirestore.getFirestoreCollectionFlow(collectionName: String): Flow<List<T>> =
    this.collection(collectionName)
        .snapshots
        .map { querySnapshot ->
            querySnapshot.documents.map { it.data<T>() }
        }
        .catch { exception ->
            if (exception is FirebaseFirestoreException)
                emit(emptyList())
            else
                throw exception
        }

suspend inline fun <reified T> FirebaseFirestore.getFirestoreCollection(collectionName: String): List<T> =
    this.collection(collectionName)
        .get()
        .documents
        .map { it.data() }

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
        .catch { exception ->
            if (exception is FirebaseFirestoreException)
                emit(null)
            else
                throw exception
        }

inline fun <reified T> FirebaseFirestore.getFirestoreDocument(
    collectionName: String,
    documentId: String,
): Flow<T?> = documentId.takeIf { it.isNotBlank() }?.let {
    this.collection(collectionName)
        .document(documentId)
        .snapshots
        .map { it.data<T?>() }
        .catch { exception ->
            if (exception is FirebaseFirestoreException)
                emit(null)
            else
                throw exception
        }
} ?: flowOf(null)

inline fun <reified T> FirebaseFirestore.getFirestoreFirstAvailableDocument(
    collectionName: String
): Flow<T?> =
    this.collection(collectionName)
        .snapshots
        .map { querySnapshot ->
            querySnapshot.documents.firstOrNull()?.data<T>()
        }
        .catch { exception ->
            if (exception is FirebaseFirestoreException)
                emit(null)
            else
                throw exception
        }

suspend inline fun <reified T : Any> FirebaseFirestore.saveObject(
    collectionName: String,
    id: String,
    data: T
) = id.takeIf { it.isNotBlank() }?.let {
    this.collection(collectionName)
        .document(id)
        .set(data)
} ?: Unit

suspend inline fun FirebaseFirestore.deleteObject(
    collectionName: String,
    id: String,
) = id.takeIf { it.isNotBlank() }?.let {
    this.collection(collectionName)
        .document(id)
        .delete()
} ?: Unit

suspend inline fun FirebaseFirestore.checkIfDocumentExists(
    collectionName: String,
    documentId: String,
) =
    if (documentId.isBlank()) false
    else
        this.collection(collectionName)
            .document(documentId)
            .get()
            .exists

inline fun <reified T> DocumentSnapshot.dataOrNull(): T? {
    return if (this.exists) this.data<T>() else null
}
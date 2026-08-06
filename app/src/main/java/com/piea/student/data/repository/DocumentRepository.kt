package com.piea.student.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.piea.student.data.model.DocumentItem
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun uploadDocument(documentType: String, fileName: String, bytes: ByteArray): Resource<DocumentItem> {
        val uid = auth.currentUser?.uid ?: return Resource.Error("Not signed in.")
        return try {
            val ref = storage.reference.child("${Constants.STORAGE_DOCUMENTS}/$uid/${System.currentTimeMillis()}_$fileName")
            ref.putBytes(bytes).await()
            val url = ref.downloadUrl.await().toString()

            val docRef = firestore.collection(Constants.COLLECTION_USERS)
                .document(uid).collection("documents").document()

            val item = DocumentItem(
                id = docRef.id,
                userId = uid,
                documentType = documentType,
                fileName = fileName,
                fileUrl = url
            )
            docRef.set(item).await()
            Resource.Success(item)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Upload failed.")
        }
    }

    suspend fun getMyDocuments(): Resource<List<DocumentItem>> {
        val uid = auth.currentUser?.uid ?: return Resource.Error("Not signed in.")
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_USERS)
                .document(uid).collection("documents").get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(DocumentItem::class.java)?.copy(id = it.id) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load documents.")
        }
    }
}

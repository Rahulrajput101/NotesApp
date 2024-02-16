package com.ondevop.notesapp.feature_note.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseNoteRepository {
    // private val userId =
    override suspend fun getNotes(): List<Note> {
        val userId = firebaseAuth.currentUser?.uid ?: return emptyList()
//       return firestore.collection("users")
//            .document(userId)
//            .collection("notes")
//            .get()
//            .await()
//            .documents.map{
//                Note(
//                    id = it.data?.get("id") as String? ?: "",
//                    title = it.data?.get("title") as String?: "",
//                    content = it.data?.get("content") as String?: "",
//                    timeStamp = it.data?.get("timeStamp") as Long?: 0L,
//                    color = it.data?.get("color") as Int?: 1,
//                   //  color = it.getLong("color")?.toInt() ?: 0
//                )
//            }.reversed()

         return firestore.collection("users")
            .document(userId)
            .collection("notes")
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                  Note(
                    id = document.getString("id") ?:  return@mapNotNull null,
                    title = document.getString("title") ?: "",
                    content = document.getString("content") ?: "",
                    timeStamp = document.getLong("timeStamp") ?: 0L,
                    color = document.getLong("color")?.toInt() ?: 1
                  )
            }.reversed()
    }


override suspend fun addNotes(message: Note) {
    Log.d("Tag", " message id  ${message.id}")
    if (message.id.isNotEmpty()) {
        firestore.collection("users")
            .document(firebaseAuth.currentUser?.uid!!)
            .collection("notes")
            .document(message.id)
            .set(message, SetOptions.merge())
            .await()
    } else {
        Log.d("Tag", " message id is null  ${message.id}")
    }
}

override suspend fun deleteNotes(id: String) {
    val userId = firebaseAuth.currentUser?.uid ?: return
    val query = firestore.collection("users")
        .document(userId)
        .collection("notes")
        .whereEqualTo("id", id)

    query.get().addOnSuccessListener { documents ->
        for (document in documents) {
            document.reference.delete()
        }
    }.await()

    // Directly Deleting a Document
//        firestore.collection("users")
//            .document(firebaseAuth.currentUser?.uid!!)
//            .collection("notes")
//            .document(noteId)
//            .delete()
//            .await()
}

}
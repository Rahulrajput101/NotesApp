package com.ondevop.notesapp.feature_note.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.ui.theme.LightGreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseNoteRepository {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    override val notes: StateFlow<List<Note>>
        get() = _notes.asStateFlow()

    private var notesListenerRegistration: ListenerRegistration? = null

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
                    id = document.getString("id") ?: return@mapNotNull null,
                    title = document.getString("title") ?: "",
                    content = document.getString("content") ?: "",
                    timeStamp = document.getLong("timeStamp") ?: 0L,
                    color = document.getLong("color")?.toInt() ?: 1
                )
            }.reversed()
    }


    override suspend fun addNotes(message: Note) {
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

    override suspend fun getNotesById(noteId: String): Note?{
        val userId = firebaseAuth.currentUser?.uid ?: return null
         try {
            val querySnapshot = firestore.collection("users")
                .document(userId)
                .collection("notes")
                .whereEqualTo("id", noteId)
                .get()
                .await()

            val documents = querySnapshot.documents

            if (documents.isNotEmpty()) {
                val document = documents[0]
                val id = document.getString("id") ?: return null
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val timeStamp = document.getLong("timeStamp") ?: 0L
                val color = document.getLong("color")?.toInt() ?: 1

               return Note(id, title, content, timeStamp, color)
            } else {
                 return null // Note with the given ID does not exist
            }
        } catch (e: Exception) {
            // Handle any exceptions, such as Firestore errors or network issues
            Log.e(TAG, "Error getting note by ID", e)
           return null
        }
    }

    override suspend fun realtimeNotesData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        // Ensure previous listener is removed before setting up a new one
        stopNotesRealtimeUpdates()
        val query = firestore.collection("users")
            .document(userId)
            .collection("notes")

        query.addSnapshotListener{querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                // Handle error
                return@addSnapshotListener
            }
            val notes = mutableListOf<Note>()
            querySnapshot?.documents?.forEach { document ->
                val id = document.getString("id") ?: ""
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val timeStamp = document.getLong("timeStamp") ?: 0L
                val color = document.getLong("color")?.toInt() ?: LightGreen.toArgb()

                val note = Note(id, title, content, timeStamp, color)
                notes.add(note)
            }
            _notes.value = notes
        }


    }


   override fun stopNotesRealtimeUpdates() {
        notesListenerRegistration?.remove()
    }

    

}
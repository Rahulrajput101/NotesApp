package com.ondevop.notesapp.feature_note.data.repository

import android.util.Log
import com.google.android.play.integrity.internal.o
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): FirebaseNoteRepository {
   // private val userId =
    override suspend fun getNotes(conversationId: String): List<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun addNotes(message: Note) {
        Log.d("Tag", " message id  ${message.id}")
        if(message.id.isNotEmpty()){
            firestore.collection("users")
                .document(firebaseAuth.currentUser?.uid!!)
                .collection("notes")
                .document(message.id)
                .set(message , SetOptions.merge())
                .await()
        }else{
            Log.d("Tag", " message id is null  ${message.id}")
        }
    }

    override suspend fun deleteNotes(id: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val query = firestore.collection("users")
            .document(userId)
            .collection("notes")
            .whereEqualTo("id", id)

        query.get().addOnSuccessListener {documents ->
            for(document in documents){
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
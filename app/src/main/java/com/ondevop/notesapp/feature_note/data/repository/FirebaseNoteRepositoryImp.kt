package com.ondevop.notesapp.feature_note.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.ui.theme.LightGreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
    private val firebaseAuth: FirebaseAuth
) : FirebaseNoteRepository {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    override val notes: StateFlow<List<Note>>
        get() = _notes.asStateFlow()

    private val _images= MutableStateFlow<List<String>>(emptyList())
    override val images: StateFlow<List<String>>
        get() = _images.asStateFlow()

    private var notesListenerRegistration: ListenerRegistration? = null

    companion object{
        val IMAGE_PATH = "${FirebaseAuth.getInstance().currentUser?.uid}/images/"
        const val IMAGE_EXT = ".png"
    }


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
                    color = document.getLong("color")?.toInt() ?: 1,
                    imageId = document.getString("imageId") ?: ""
                )
            }.reversed()
    }


    override suspend fun addNotes(message: Note,image : String?) {
        if (message.id.isNotEmpty() && !image.isNullOrBlank()) {
            firestore.collection("users")
                .document(firebaseAuth.currentUser?.uid!!)
                .collection("notes")
                .document(message.id)
                .set(message, SetOptions.merge())
                .await()

             image?.let {
                 firebaseStorage.child(IMAGE_PATH + message.imageId + IMAGE_EXT)
                     .putFile(it.toUri()).await()
             }

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

    override suspend fun getNotesById(noteId: String): Pair<Note?,String?>{
        val userId = firebaseAuth.currentUser?.uid ?: return Pair(null,null)
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
                val id = document.getString("id") ?: return Pair(null,null)
                val title = document.getString("title") ?: ""
                val content = document.getString("content") ?: ""
                val timeStamp = document.getLong("timeStamp") ?: 0L
                val color = document.getLong("color")?.toInt() ?: 1
                val imageId = document.getString("imageId") ?: ""


                val imageUri = firebaseStorage.child(IMAGE_PATH + imageId + IMAGE_EXT).downloadUrl.await()

               return Pair(Note(id, title, content, timeStamp, color,imageId),imageUri.toString())
            } else {
                 return Pair(null,null) // Note with the given ID does not exist
            }
        } catch (e: Exception) {
            // Handle any exceptions, such as Firestore errors or network issues
            Log.e(TAG, "Error getting note by ID", e)
           return Pair(null,null)
        }
    }

    override suspend fun realtimeNotesData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        // Ensure previous listener is removed before setting up a new one
        stopNotesRealtimeUpdates()
        Log.d("im"," rund")
      val images = firebaseStorage.child(IMAGE_PATH).listAll().await()
       images.items.forEach {
           Log.d("im"," ${images.items}")
       }
        Log.d("im"," run")
        val query = firestore.collection("users")
            .document(userId)
            .collection("notes")

        query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
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
                val imageId = document.getString("imageId") ?: ""
                val note = Note(id, title, content, timeStamp, color, imageId)
                if (imageId.isNotEmpty()) {
                    Log.d("Tag", IMAGE_PATH + imageId + IMAGE_EXT)
                   val v =  firebaseStorage.child(IMAGE_PATH + imageId + IMAGE_EXT).downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.d("Tag2", "${uri.toString()}" )
                            _images.value = listOf(uri.toString())
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors in fetching image URLs from Firebase Storage
                            Log.e(TAG, "Error getting image URL from Firebase Storage", exception)
                        }
                }
                notes.add(note)
            }

            _notes.value = notes
        }


    }
//    override suspend fun realtimeNotesData() {
//        val userId = firebaseAuth.currentUser?.uid ?: return
//        // Ensure previous listener is removed before setting up a new one
//        stopNotesRealtimeUpdates()
//
//        val query = firestore.collection("users")
//            .document(userId)
//            .collection("notes")
//
//        try {
//            val querySnapshot = query.get().await()
//            val notes = mutableListOf<Note>()
//            val imageTasks = mutableListOf<Task<Uri>>() // List to store image download tasks
//
//            for (document in querySnapshot.documents) {
//                val id = document.getString("id") ?: ""
//                val title = document.getString("title") ?: ""
//                val content = document.getString("content") ?: ""
//                val timeStamp = document.getLong("timeStamp") ?: 0L
//                val color = document.getLong("color")?.toInt() ?: LightGreen.toArgb()
//                val imageId = document.getString("imageId") ?: ""
//                val note = Note(id, title, content, timeStamp, color, imageId)
//                notes.add(note)
//
//                if (imageId.isNotEmpty()) {
//                    val downloadTask = firebaseStorage.child(IMAGE_PATH + imageId + IMAGE_EXT)
//                        .downloadUrl
//                        .addOnSuccessListener { uri ->
//                            _images.value = _images.value + listOf(uri.toString()) // Append to the existing images list
//                        }
//                        .addOnFailureListener { exception ->
//                            // Handle errors in fetching image URLs from Firebase Storage
//                            Log.e(TAG, "Error getting image URL from Firebase Storage", exception)
//                        }
//                    imageTasks.add(downloadTask)
//                }
//            }
//
//            // Wait for all image download tasks to complete
//
//            // Update notes after all image download tasks are complete
//            _notes.value = notes
//        } catch (e: Exception) {
//            // Handle exceptions
//            Log.e(TAG, "Error fetching realtime notes data", e)
//        }
//    }



    private suspend fun getImageUrl(imageId: String): String? {
        if (imageId.isEmpty()) {
            return null
        }

        return try {
            val imageUri = firebaseStorage.child(IMAGE_PATH + imageId + IMAGE_EXT).downloadUrl.await()
            imageUri.toString()
        } catch (e: Exception) {
            // Handle errors in retrieving image URL from Firebase Storage
            Log.e(TAG, "Error getting image URL from Firebase Storage", e)
            null
        }
    }
    override suspend fun saveImage(noteId: String) {
        TODO("Not yet implemented")
    }


    override fun stopNotesRealtimeUpdates() {
        notesListenerRegistration?.remove()
    }

    

}
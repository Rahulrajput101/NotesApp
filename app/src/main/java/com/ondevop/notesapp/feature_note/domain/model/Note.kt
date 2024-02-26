package com.ondevop.notesapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.errorprone.annotations.Keep
import com.ondevop.notesapp.ui.theme.*
import java.util.Date

@Keep
@Entity(
    tableName ="note"
)
data class Note(
    @PrimaryKey(autoGenerate = false)
    var id: String = Date().time.toString(),
    val title : String,
    val content : String,
    val timeStamp : Long,
    val color :  Int,
    val imageId: String? = System.currentTimeMillis().toString()
){
    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}


// Migration script to update existing documents in Firestore
//val collectionRef = firestore.collection("your_collection")
//
//collectionRef.get().addOnSuccessListener { querySnapshot ->
//    for (document in querySnapshot.documents) {
//        val note = document.toObject(Note::class.java)
//        if (note != null) {
//            val updatedNote = note.copy(imageId = "default_image_id") // Set a default value for imageId
//            collectionRef.document(document.id).set(updatedNote)
//        }
//    }
//}
class InvalidNoteException(message : String) : Exception(message)
class CachingExceptionFirebase : Exception()

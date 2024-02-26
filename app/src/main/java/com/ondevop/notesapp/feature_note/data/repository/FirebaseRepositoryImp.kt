package com.ondevop.notesapp.feature_note.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.model.UserInfo
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseRepository
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseRepository {
    override fun saveUser(userInfo: UserInfo) {
        val firebaseUserId = firebaseAuth.currentUser?.uid ?: return

        try {
            val userCollection = firestore.collection("users")

            val data = mutableMapOf<String, Any>(
                Pair("id", firebaseUserId),
                Pair("name", userInfo.userName),
                Pair("email", userInfo.email)
            )

            userCollection.document(firebaseUserId)
                .set(data, SetOptions.merge())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun saveNote(text : String){
        val firebaseUserId = firebaseAuth.currentUser?.uid ?: return

        try {

        }catch ( e: Exception){
            e.printStackTrace()
        }
    }

//    override suspend fun getUser(): UserInfo? {
//        val firebaseUserId = firebaseAuth.currentUser?.uid ?: return null
//
//        val userCollection = firestore.collection("users")
//
//        val documentSnapshot = userCollection.document(firebaseUserId)
//            .get()
//            .await()
//
//      return documentSnapshot.toObject(UserInfo::class.java)
//
//    }

    override suspend fun getUser(): UserInfo? {
        val firebaseUserId = firebaseAuth.currentUser?.uid ?: return null

        val userCollection = firestore.collection("users")

        return userCollection.document(firebaseUserId)
            .get()
            .await()
            .data?.let { mutableMap ->
                UserInfo(
                    userName = mutableMap["name"] as String? ?: "",
                    email = mutableMap["email"] as String? ?: "",
                    profileUri = null,
                )
            }

    }

    override suspend fun getAllTheUsers(): List<UserInfo> {
        val usersCollection = firestore.collection("users")
        val userList = mutableListOf<UserInfo>()

        usersCollection.get().await().forEach { document ->
            val userData = document.data
            val userInfo = UserInfo(
                userName = userData["name"] as? String ?: "",
                email = userData["email"] as? String ?: "",
                profileUri = null
            )
            userList.add(userInfo)
        }

        return userList
    }

//    override suspend fun getAllTheUsers(): List<UserInfo> {
//        val userCollection = firestore.collection("users")
//        return try {
//
//            val querySnapshot = userCollection.get().await()
//            val userList  = mutableListOf<UserInfo>()
//
//            for ( documents in querySnapshot){
//                val userInfo = documents.toObject(UserInfo::class.java)
//                userInfo?.let { userList.add(it) }
//            }
//            userList
//        } catch ( e: Exception){
//            e.printStackTrace()
//            return emptyList()
//        }
//    }

    fun migrateNotesWithImageId() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("your_collection")

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val note = document.toObject(Note::class.java)
                if (note != null) {
                    // Update the note with the new field (imageId)
                    val updatedNote = note.copy(imageId = "default_image_id") // Set a default value for imageId
                    collectionRef.document(document.id).set(updatedNote)
                        .addOnSuccessListener {
                            println("Document ${document.id} updated successfully")
                        }
                        .addOnFailureListener { e ->
                            println("Error updating document ${document.id}: $e")
                        }
                }
            }
        }
    }




}
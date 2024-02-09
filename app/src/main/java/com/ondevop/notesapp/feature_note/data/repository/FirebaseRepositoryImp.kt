package com.ondevop.notesapp.feature_note.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ondevop.notesapp.feature_note.domain.model.UserInfo
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseRepository

class FirebaseRepositoryImp(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseRepository {
    override fun saveUser(userInfo: UserInfo) {
        val firebaseUserId = firebaseAuth.currentUser?.uid ?: return

        try {
           val userCollection =  firestore.collection("users")

            val data = mutableMapOf<String, Any>(
                Pair("id", firebaseUserId),
                Pair("name", userInfo.userName),
                Pair("email", userInfo.email)
            )

            userCollection.document(firebaseUserId)
                .set(data, SetOptions.merge())

        }catch (e : Exception){
              e.printStackTrace()
        }

    }
}
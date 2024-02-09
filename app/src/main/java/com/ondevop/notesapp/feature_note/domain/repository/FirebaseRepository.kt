package com.ondevop.notesapp.feature_note.domain.repository

import com.ondevop.notesapp.feature_note.domain.model.UserInfo

interface FirebaseRepository  {

    fun saveUser( userInfo: UserInfo)

    suspend fun getUser() : UserInfo?
    suspend fun getAllTheUsers(): List<UserInfo>
}
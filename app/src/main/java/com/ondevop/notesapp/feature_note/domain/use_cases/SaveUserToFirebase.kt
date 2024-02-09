package com.ondevop.notesapp.feature_note.domain.use_cases


import com.ondevop.notesapp.feature_note.domain.model.UserInfo
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseRepository

class SaveUserToFirebase(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userInfo: UserInfo){
        repository.saveUser(userInfo)
    }
}
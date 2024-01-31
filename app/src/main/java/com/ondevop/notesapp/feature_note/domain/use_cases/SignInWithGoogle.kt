package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.UserInfo
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInWithGoogle  @Inject constructor(
    val repository: NoteRepository
) {
    suspend operator fun invoke(idToken : String) : Result<UserInfo> {
        return repository.loginWithGoogle(idToken)
    }
}
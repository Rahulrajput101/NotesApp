package com.ondevop.notesapp.feature_note.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.ondevop.notesapp.feature_note.domain.model.UserInfo
import com.ondevop.notesapp.feature_note.data.data_source.NoteDao
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepositoryImp (
    private val dao: NoteDao,
    private val firebaseAuth: FirebaseAuth
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNotesById(id: String): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun DeleteNote(note: Note) {
        return dao.deleteNote(note)
    }

    override suspend fun loginWithGoogle(id: String): Result<UserInfo> {
        return try {
            val credential = GoogleAuthProvider.getCredential(id, null)
            val result = firebaseAuth.signInWithCredential(credential).await()

            if (result.user != null) {
                val user = result.user!!
                Result.success(
                     UserInfo(
                        userName = user.displayName ?: "",
                        profileUri = user.photoUrl,
                         email = user.email ?: ""
                    )
                )
            } else {
                Result.failure(Exception("Google Login Failed"))
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("User does not exist"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid Google credentials"))
        } catch (e: FirebaseException) {
            Result.failure(Exception(e.localizedMessage ?: "Google Login Failed"))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
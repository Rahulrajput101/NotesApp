package com.ondevop.notesapp.feature_note.domain.repository

import com.ondevop.notesapp.feature_note.domain.model.UserInfo
import com.ondevop.notesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {


    fun getNotes() : Flow<List<Note>>

    suspend fun getNotesById(id : String) : Note?

    suspend fun insertNote(note: Note)

    suspend fun DeleteNote(note: Note)


    suspend fun loginWithGoogle(id : String): Result<UserInfo>


}
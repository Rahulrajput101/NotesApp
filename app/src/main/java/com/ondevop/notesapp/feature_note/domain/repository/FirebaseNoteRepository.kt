package com.ondevop.notesapp.feature_note.domain.repository

import com.ondevop.notesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.StateFlow

interface FirebaseNoteRepository {

    val notes : StateFlow<List<Note>>
    suspend fun getNotes(): List<Note>
    suspend fun addNotes(message: Note)
    suspend fun deleteNotes(noteId: String)

    suspend fun getNotesById(noteId: String): Note?

    suspend fun realtimeNotesData()

     fun stopNotesRealtimeUpdates()
}
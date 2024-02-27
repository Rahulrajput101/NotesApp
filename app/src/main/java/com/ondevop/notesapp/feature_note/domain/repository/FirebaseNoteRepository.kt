package com.ondevop.notesapp.feature_note.domain.repository

import com.ondevop.notesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.StateFlow

interface FirebaseNoteRepository {

    val notes : StateFlow<List<Note>>
    val images : StateFlow<List<String>>
    suspend fun getNotes(): List<Note>
    suspend fun addNotes(message: Note, image: String?)
    suspend fun deleteNotes(noteId: String)

    suspend fun getNotesById(noteId: String): Pair<Note?,String?>

    suspend fun realtimeNotesData()

    suspend fun saveImage(noteId: String,)

     fun stopNotesRealtimeUpdates()
}
package com.ondevop.notesapp.feature_note.domain.repository

import com.ondevop.notesapp.feature_note.domain.model.Note

interface FirebaseNoteRepository {
    suspend fun getNotes(): List<Note>
    suspend fun addNotes(message: Note)
    suspend fun deleteNotes(noteId: String)
}
package com.ondevop.notesapp.feature_note.domain.repository

import com.ondevop.notesapp.feature_note.domain.model.Note

interface FirebaseNoteRepository {
    suspend fun getNotes(conversationId: String): List<Note>
    suspend fun addNotes(message: Note)
    suspend fun deleteNotes(conversationId: String)
}
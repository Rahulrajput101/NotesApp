package com.ondevop.notesapp.feature_note.data.repository

import com.ondevop.notesapp.feature_note.data.data_source.NoteDao
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImp (
    private val dao: NoteDao

) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNotesById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun DeleteNote(note: Note) {
        return dao.deleteNote(note)
    }
}
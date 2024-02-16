package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository

class GetNoteUseCase (
   private val repository: NoteRepository,
    private val firebaseNoteRepository: FirebaseNoteRepository
        ) {

    suspend  operator fun invoke(id : String): Note?{
       return repository.getNotesById(id)

    }
}
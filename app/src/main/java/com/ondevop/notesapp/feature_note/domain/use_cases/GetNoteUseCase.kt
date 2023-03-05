package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository

class GetNoteUseCase (
   private val repository: NoteRepository
        ) {

    suspend  operator fun invoke(id : Int): Note?{

       return repository.getNotesById(id)

    }
}
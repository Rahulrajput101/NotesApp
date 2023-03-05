package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class DeleteNoteUseCase(
   private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note)  {

          repository.DeleteNote(note)


    }





}
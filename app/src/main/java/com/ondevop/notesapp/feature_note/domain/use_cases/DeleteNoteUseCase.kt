package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class DeleteNoteUseCase(
   private val repository: NoteRepository,
    private val firebaseNoteRepository: FirebaseNoteRepository
) {
    suspend operator fun invoke(note: Note)  {
          firebaseNoteRepository.deleteNotes(note.id)
         // repository.DeleteNote(note)
    }





}
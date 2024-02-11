package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.InvalidNoteException
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository,
    private val firebaseNoteRepository: FirebaseNoteRepository
) {


    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()){
          throw InvalidNoteException("The title of the note can't be empty.")
        }

        if(note.content.isBlank()){
            throw InvalidNoteException("The content of the note can't be empty.")
        }
        val id =  repository.insertNote(note)
        firebaseNoteRepository.addNotes(note)

    }
}
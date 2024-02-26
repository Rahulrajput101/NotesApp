package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository

class AddNoteToFirebase (
    private val firebaseNoteRepository: FirebaseNoteRepository
){
    suspend operator fun invoke(note: Note, image: String? = null){
        firebaseNoteRepository.addNotes(note,image)
    }
}
package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.data.repository.FirebaseNoteRepositoryImp
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository

class RegisterNotesRealTimeUpdates(
    private val firebaseNoteRepository: FirebaseNoteRepository
) {

    suspend operator fun invoke(){
        firebaseNoteRepository.realtimeNotesData()
    }

}
package com.ondevop.notesapp.feature_note.domain.use_cases

import com.ondevop.notesapp.feature_note.data.repository.FirebaseNoteRepositoryImp
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository

class UnregisterNotesRealTimeUpdates(
    private val firebaseNoteRepository: FirebaseNoteRepository
) {

    operator fun invoke(){
        firebaseNoteRepository.stopNotesRealtimeUpdates()
    }

}
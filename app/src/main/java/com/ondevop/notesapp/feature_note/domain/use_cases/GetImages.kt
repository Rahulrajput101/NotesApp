package com.ondevop.notesapp.feature_note.domain.use_cases

import androidx.compose.runtime.collectAsState
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.feature_note.domain.util.NoteOrder
import com.ondevop.notesapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow

class GetImages(
    private val firebaseNoteRepository: FirebaseNoteRepository
) {
     operator fun invoke(
    ): Flow<List<String>> {

        return firebaseNoteRepository.images
    }

}
package com.ondevop.notesapp.feature_note.domain.use_cases

import android.util.Log
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.repository.FirebaseNoteRepository
import com.ondevop.notesapp.feature_note.domain.repository.NoteRepository
import com.ondevop.notesapp.feature_note.domain.util.NoteOrder
import com.ondevop.notesapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository,
    private val firebaseNoteRepository: FirebaseNoteRepository
) {

    suspend operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        Log.d("Get", "notes: ${firebaseNoteRepository.getNotes()}")
        val sortedNote = firebaseNoteRepository.notes.map { notes ->

            // val sortedNote = repository.getNotes().map {notes->

            when (noteOrder.orderType) {

                is OrderType.Ascending -> {
                    when (noteOrder) {

                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timeStamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }

                    }
                }
            }
        }
        return sortedNote
    }

}
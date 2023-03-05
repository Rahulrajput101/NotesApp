package com.ondevop.notesapp.feature_note.presentation.notes

import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.util.NoteOrder
import com.ondevop.notesapp.feature_note.domain.util.OrderType

data class NoteState (

    val notes : List<Note> = emptyList(),
    val order: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isSelectionAvailable : Boolean = false

)
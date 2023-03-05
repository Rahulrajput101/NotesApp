package com.ondevop.notesapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddNoteUiEvent {

    data class EnteredTitle(val value : String) : AddNoteUiEvent()
    data class ChangeTitleFocus(val focusState : FocusState) : AddNoteUiEvent()
    data class EnteredContent(val value : String) : AddNoteUiEvent()
    data class ChangeContentFocus(val focusState : FocusState) : AddNoteUiEvent()
    data class ChangeColor(val color : Int) : AddNoteUiEvent()

    object SaveNote : AddNoteUiEvent()
}
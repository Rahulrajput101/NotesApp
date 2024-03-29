package com.ondevop.notesapp.feature_note.presentation.util

sealed class UiEvent {

    object Success: UiEvent()
    data class ShowSnackbar(val message : UiText): UiEvent()
    object NavigateUp: UiEvent()
}


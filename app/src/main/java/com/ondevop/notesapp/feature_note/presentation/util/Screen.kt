package com.ondevop.notesapp.feature_note.presentation.util

sealed class Screen(val route : String){

    object  NoteScreen : Screen("note_screen")

    object AddEditNoteScreen : Screen("add_edit_note_screen")
}

package com.ondevop.notesapp.feature_note.presentation.add_edit_note

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ondevop.notesapp.feature_note.domain.model.InvalidNoteException
import com.ondevop.notesapp.feature_note.domain.model.Note
import com.ondevop.notesapp.feature_note.domain.use_cases.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    val notesUseCases: NotesUseCases,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter content..."
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf<Int>(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private var currentNoteId: String = Date().time.toString()

    private val _imageUri = MutableStateFlow("")
    val imageUri = _imageUri.asStateFlow()

    private val _eventFlow = Channel<NoteUiEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            if (noteId.isNotEmpty()) {
                viewModelScope.launch {
                    notesUseCases.getNoteUseCase(noteId).also { (note,image) ->
                        note?.let {
                            currentNoteId = note.id
                            _noteTitle.value = noteTitle.value.copy(
                                text = note.title,
                                isHintVisible = false
                            )
                            _noteContent.value = noteContent.value.copy(
                                text = note.content,
                                isHintVisible = false
                            )
                            _noteColor.value = note.color
                        }
                       image?.let {
                           _imageUri.value = image
                       }
                    }
                }
            }
        }
    }


    fun onEvent(event: AddNoteUiEvent) {

        when (event) {

            is AddNoteUiEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.value)
            }

            is AddNoteUiEvent.ChangeTitleFocus -> {
                _noteTitle.value =
                    noteTitle.value.copy(isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank())
            }

            is AddNoteUiEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }

            is AddNoteUiEvent.ChangeContentFocus -> {
                _noteContent.value =
                    noteContent.value.copy(isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank())
            }

            is AddNoteUiEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddNoteUiEvent.SaveNote -> {
                viewModelScope.launch() {
                    try {
                        val note = Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId,
                            )
                       // notesUseCases.addNoteUsesCase(note)

                        async { notesUseCases.addNoteToFirebase(note,imageUri.value) }

                        _eventFlow.send(NoteUiEvent.saveNote)


                    } catch (e: InvalidNoteException) {
                        _eventFlow.send(
                            NoteUiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save message"
                            )
                        )
                    }
                }
            }

            is AddNoteUiEvent.UpdateProfileUir -> {
                _imageUri.update {
                    event.profileUri
                }
            }
        }
    }

    sealed class NoteUiEvent {
        data class ShowSnackbar(val message: String) : NoteUiEvent()
        object saveNote : NoteUiEvent()
    }


}
package com.ondevop.notesapp.feature_note.presentation.notes


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ondevop.notesapp.feature_note.domain.use_cases.NotesUseCases
import com.ondevop.notesapp.feature_note.domain.util.NoteOrder
import com.ondevop.notesapp.feature_note.domain.util.OrderType
import com.ondevop.notesapp.feature_note.domain.model.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases
) : ViewModel() {


    private val _state = mutableStateOf(NoteState())
    val state : State<NoteState> =_state

    private var recentlyDeletedNote : Note? = null

    var getNotesJob : Job? = null



    init{
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent( noteEvent: NoteEvent){

        when(noteEvent){

            is NoteEvent.Order -> {

                if(state.value.order::class == noteEvent.noteOrder::class && state.value.order.orderType==noteEvent.noteOrder.orderType){

                    return

                }

                getNotes(noteEvent.noteOrder)




            }

            is NoteEvent.DeleteNote -> {
              viewModelScope.launch {
                  notesUseCases.deleteNoteUseCase(noteEvent.note)
                  recentlyDeletedNote = noteEvent.note
              }


            }

            is NoteEvent.RestoreNote -> {
                viewModelScope.launch {
                   notesUseCases.addNoteUsesCase(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null

                }


            }

            is NoteEvent.ToggleOrderSelection -> {
                _state.value = state.value.copy(
                    isSelectionAvailable = !state.value.isSelectionAvailable
                )
            }






        }


    }


    private fun getNotes(noteOrder: NoteOrder){
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotesUseCase(noteOrder).onEach {notes->
            _state.value = state.value.copy(
                order =noteOrder,
                notes =notes
            )

        }.launchIn(viewModelScope)

    }





}
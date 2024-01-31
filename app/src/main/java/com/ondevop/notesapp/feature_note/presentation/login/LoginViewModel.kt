package com.ondevop.notesapp.feature_note.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ondevop.core_domain.prefernces.Preferences
import com.ondevop.notesapp.feature_note.domain.use_cases.NotesUseCases
import com.ondevop.notesapp.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.ondevop.notesapp.feature_note.presentation.util.UiEvent
import com.ondevop.notesapp.feature_note.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: Preferences,
    private val notesUseCases: NotesUseCases
): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LogInEvent){
        when(event){
            is LogInEvent.SaveUserdata -> {
                viewModelScope.launch {
                    preferences.saveUserName(event.userData.userName ?: "")
                    if(event.userData.profilePictureUrl != null){
                        preferences.saveProfileUri(event.userData.profilePictureUrl)
                    }
                    _uiEvent.send(UiEvent.Success)
                }
            }
            is LogInEvent.SignInUnsuccessful -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = false)
                    _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString(event.errorMessage)))
                }
            }
            is LogInEvent.SignInWithGoogle -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    notesUseCases.signInWithGoogle(event.idToken).onSuccess {
                        preferences.saveLoggedInfo(true)
                        preferences.saveUserName(it.userName)
                        val profileUri = it.profileUri.toString()
                        preferences.saveProfileUri(profileUri)
                        _state.value = _state.value.copy(isLoading = false)
                        _uiEvent.send(UiEvent.Success)
                    }.onFailure {
                        _state.value = _state.value.copy(isLoading = false)
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString(it.message.toString())))
                    }
                }
            }
            else ->{ }
        }
    }
}
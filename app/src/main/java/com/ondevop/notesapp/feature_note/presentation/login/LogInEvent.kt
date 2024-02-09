package com.ondevop.notesapp.feature_note.presentation.login

sealed class LogInEvent {

    data class SignInWithGoogle(val idToken : String, val userData: UserData) : LogInEvent()
    data class SignInUnsuccessful(val errorMessage: String) : LogInEvent()

    data class SaveUserdata(val userData: UserData): LogInEvent()
}

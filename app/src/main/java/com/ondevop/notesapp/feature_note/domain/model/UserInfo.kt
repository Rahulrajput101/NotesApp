package com.ondevop.notesapp.feature_note.domain.model

import android.net.Uri

data class UserInfo(
    val userName : String,
    val profileUri: Uri?,
    val email : String,
)

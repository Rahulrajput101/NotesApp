package com.ondevop.notesapp.feature_note.presentation.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.ondevop.notesapp.feature_note.presentation.util.UiEvent


@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    googleSignInClient: GoogleSignInClient
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateUp -> {
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }

                UiEvent.Success -> {
                    navigateToHome()
                }
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val userData = account.id?.let {
                        UserData(
                            userName = account.displayName,
                            profilePictureUrl = account.photoUrl.toString(),
                        )
                    }
                    account.idToken?.let { idToken ->
                        viewModel.onEvent(LogInEvent.SignInWithGoogle(idToken))
                    }

                } else {
                    // Handle sign-in failure
                    viewModel.onEvent(LogInEvent.SignInUnsuccessful("Failed to sign in"))
                }
            } catch (e: ApiException) {
                viewModel.onEvent(LogInEvent.SignInUnsuccessful("$e"))
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            // Sign-in was canceled
            viewModel.onEvent(LogInEvent.SignInUnsuccessful("Sign-in canceled"))
            // Sign-in was canceled
        } else {
            // Handle other result codes or errors, if needed
            viewModel.onEvent(LogInEvent.SignInUnsuccessful("Sign-in failed with error code ${result.resultCode}"))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                RoundedButton(
                    text = "G",
                    onClick = {
                        launcher.launch(googleSignInClient.signInIntent)
                    }
                )
                Text(
                    text = "google",
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
        }
    }

}

package com.ondevop.notesapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ondevop.core_domain.prefernces.Preferences
import com.ondevop.notesapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.ondevop.notesapp.feature_note.presentation.login.LoginScreen
import com.ondevop.notesapp.feature_note.presentation.notes.NoteScreen
import com.ondevop.notesapp.feature_note.presentation.util.Screen
import com.ondevop.notesapp.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences
    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isLoggedIn = preferences.getLoggedInfo().first()

            setContent {
                NotesAppTheme {
                    // A surface container using the 'background' color from the theme
                    val name by preferences.getUserName().collectAsState(initial = "")

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val navController = rememberNavController()
                        val snackbarHostState = remember { SnackbarHostState() }
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        ) {
                            NavHost(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it),
                                navController = navController,
                                startDestination = if (isLoggedIn) Screen.NoteScreen.route else Screen.LoginScreen.route
                            )
                            {

                                composable(route = Screen.LoginScreen.route) {
                                    LoginScreen(
                                        snackbarHostState = snackbarHostState,
                                        navigateToHome = {
                                           navController.navigate(Screen.NoteScreen.route)
                                        },
                                        googleSignInClient =googleSignInClient
                                    )
                                }
                                composable(route = Screen.NoteScreen.route)
                                {
                                    NoteScreen(
                                        navController = navController,
                                        name = name
                                    )
                                }
                                composable(
                                    route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                                    arguments = listOf(
                                        navArgument(
                                            name = "noteId"
                                        ) {
                                            type = NavType.StringType
                                            defaultValue = ""
                                        },

                                        navArgument(
                                            name = "noteColor"
                                        ) {
                                            type = NavType.IntType
                                            defaultValue = -1
                                        },
                                    )

                                ) {
                                    val color = it.arguments?.getInt("noteColor") ?: -1

                                    AddEditNoteScreen(
                                        navController = navController,
                                        noteColor = color
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}



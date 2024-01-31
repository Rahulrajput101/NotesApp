package com.ondevop.notesapp.feature_note.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun CustomLoadingDialog(
    showDialog: Boolean,
    text : String
) {
    if (showDialog) {
        var dotCount by remember { mutableStateOf(0) }
        Dialog(
            onDismissRequest = { }
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp) // Adjust the size as needed
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp) // Adjust the size of the indicator
                        .align(Alignment.Center)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 18.sp)) {
                            append(text)
                        }
                        repeat(dotCount) {
                            append(".")
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }

        LaunchedEffect(Unit) {
            while (showDialog) {
                delay(500) // Adjust the delay as needed
                dotCount = (dotCount + 1) % 4
            }
        }
    }

}
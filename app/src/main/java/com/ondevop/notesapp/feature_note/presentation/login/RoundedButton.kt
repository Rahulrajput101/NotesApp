package com.ondevop.notesapp.feature_note.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.ondevop.notesapp.ui.theme.LocalSpacing

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(200.dp))
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(200.dp)
            )
            .background(MaterialTheme.colors.primary)
            .clickable {
                onClick()
            }
            ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
        )
    }

}
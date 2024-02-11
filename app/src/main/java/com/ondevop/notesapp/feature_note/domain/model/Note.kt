package com.ondevop.notesapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ondevop.notesapp.ui.theme.*
import java.util.Date

@Entity(
    tableName ="note"
)
data class Note(
    @PrimaryKey(autoGenerate = false)
    var id: String = Date().time.toString(),
    val title : String,
    val content : String,
    val timeStamp : Long,
    val color :  Int,
){
    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message : String) : Exception(message)

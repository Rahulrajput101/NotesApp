package com.ondevop.notesapp.feature_note.presentation.notes.components

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ondevop.notesapp.feature_note.domain.util.NoteOrder
import com.ondevop.notesapp.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange : (NoteOrder) -> Unit

){
    
    Column (modifier = modifier){
        Row(
            modifier = Modifier.fillMaxWidth()
        ){

            DefaultRadioButton(
                text ="Title" ,
                onSelected = { onOrderChange(NoteOrder.Title(noteOrder.orderType))},
                selected = noteOrder is NoteOrder.Title )

            Spacer(modifier = Modifier.width(6.dp))

            DefaultRadioButton(
                text ="Date" ,
                onSelected = { onOrderChange(NoteOrder.Date(noteOrder.orderType))},
                selected = noteOrder is NoteOrder.Date )

            Spacer(modifier = Modifier.width(6.dp))

            DefaultRadioButton(
                text ="Color" ,
                onSelected = { onOrderChange(NoteOrder.Color(noteOrder.orderType))},
                selected = noteOrder is NoteOrder.Color )

            Spacer(
                modifier =Modifier.height(15.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                DefaultRadioButton(
                    text = "Ascending",
                    onSelected = { onOrderChange(noteOrder.copy(orderType = OrderType.Ascending)) },
                    selected = noteOrder.orderType is OrderType.Ascending)
                Spacer(modifier = Modifier.width(6.dp))

                DefaultRadioButton(
                    text = "Descending",
                    onSelected = { onOrderChange(noteOrder.copy(orderType = OrderType.Descending)) },
                    selected = noteOrder.orderType is OrderType.Descending)

            }


        }




    }

}
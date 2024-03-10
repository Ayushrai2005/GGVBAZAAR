package ggv.ayush.myapplication.DrawerScreens

import android.app.Dialog
import android.graphics.Paint.Style
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.primarySurface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun LogoutDialog(dialogOpen : MutableState<Boolean>){

    if(dialogOpen.value){
        //Show dialog
        AlertDialog(
            onDismissRequest = {
                   dialogOpen.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    dialogOpen.value = false
                    //Makes user singout by using auth


                }) {
                    Text(text = "Confirm")
                    
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dialogOpen.value = false
                }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Sing Out " ,style = TextStyle(fontWeight = FontWeight.Bold)  )
            },
            text = {
                Text(text = "Confirm SignOut")
            },
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colors.primarySurface)
                .padding(8.dp),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = Color.White,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )

        )
    }

}
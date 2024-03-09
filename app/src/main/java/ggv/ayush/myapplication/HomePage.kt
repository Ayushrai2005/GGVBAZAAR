package ggv.ayush.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


@Composable
fun HomePage(navController : NavController){

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        
        Button(onClick = {navController.navigate("Login_page")}) {
            Text(text = "Sign Out")
            
        }
        Text(text = "ayush" , modifier = Modifier.fillMaxSize())
        Text(text = "ayushh" , modifier = Modifier.fillMaxSize())
        Text(text = "ayushhh" , modifier = Modifier.fillMaxSize())
        Text(text = "ayushhhh" , modifier = Modifier.fillMaxSize())
        Text(text = "ayushhhhh" , modifier = Modifier.fillMaxSize())
        Text(text = "ayushhhhhh" , modifier = Modifier.fillMaxSize())

    }
}
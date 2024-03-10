package ggv.ayush.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView( navController : NavController){

   val scaffoldState: ScaffoldState = rememberScaffoldState()
   val scope : CoroutineScope = rememberCoroutineScope()

    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Home")},
                navigationIcon = { IconButton(onClick = {
                    //Open the Drawer
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account Drawer")


                }}


            )
        }
        
    ) {
        Text(text = "Text" , modifier = Modifier.padding(it))

        Button(onClick = {navController.navigate("Login_page")}) {
            androidx.compose.material.Text(text = "Sign Out")

        }

    }


}

@Composable
fun DrawerItem(
    selected : Boolean,
    item : Screen.DrawerScreen ,
    onDrawerItemClicked : () -> Unit
){
    val background = if (selected) Color.Magenta else Color.White

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .background(background)
                .clickable {
                    onDrawerItemClicked()
                }
        ) {
            Icon(painter = painterResource(id = item.icon),
                contentDescription = item.dTitle,
                modifier = Modifier.padding(end = 8.dp , top = 4.dp)
            )
            Text(text = item.dTitle ,
                style = MaterialTheme.typography.labelMedium)



        }



}


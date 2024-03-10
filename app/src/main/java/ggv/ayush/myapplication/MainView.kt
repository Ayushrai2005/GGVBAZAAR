package ggv.ayush.myapplication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
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
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.DrawerScreens.LogoutDialog
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage
import ggv.ayush.myapplication.Screen.BottomScreen.Home.screensInBottom
import ggv.ayush.myapplication.Screen.BottomScreen.Home.screensInDrawer
import ggv.ayush.myapplication.Screen.BottomScreen.Home.title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView( navController : NavController){

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope : CoroutineScope = rememberCoroutineScope()


    val viewModel : MainViewModel = viewModel()

    //
    // Use to check which item is selected on the drawer Menu
    // Allow us to find out which "VIEW" is currently selected
    val controller : NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val currentScreen = remember{
        viewModel.currentScreen.value
    }
    val title = remember{
        //Holds CurrentScreen.Title
        mutableStateOf(currentScreen.title)

    }

    val dialogOpen = remember{
        mutableStateOf(false)
    }

    val bottomBar : @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home){
            BottomNavigation(
                Modifier.wrapContentSize()
            ) {
                screensInBottom.forEach{ item ->
                    val isSelected = currentRoute == item.bRoute
                    Log.d(
                        "Navigation",
                        "Item: ${item.bTitle}, Current Route: $currentRoute, Is Selected: $isSelected"
                    )
                    val tint = if (isSelected) Color.White else Color.Black

                    BottomNavigationItem(
                        selected = currentRoute == item.bRoute,
                        onClick = { navController.navigate(item.bRoute)
                            title.value = item.bTitle
                        },
                        icon = { Icon(contentDescription = item.bTitle , painter = painterResource(id = item.icon))
                        },
                        label = { Text(text = item.bTitle) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )

                }
            }
        }

    }

    Scaffold(
        bottomBar = bottomBar,
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
        }, scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(Modifier .padding(16.dp)) {
                items(screensInDrawer){
                    item ->
                    DrawerItem(selected = currentRoute == item.dRoute , item =  item) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        if(item.dRoute == "logout"){
                            //Open dialog to confirm logout
                            dialogOpen.value = true
                        }else{
                            navController.navigate(item.dRoute)
                            title.value= item.title
                        }

                    }
                }
            }
        }
        
    ) {
        LogoutDialog(dialogOpen = dialogOpen)

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


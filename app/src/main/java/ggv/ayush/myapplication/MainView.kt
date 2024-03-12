package ggv.ayush.myapplication
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.BottomScreens.BottomNavGraph
import ggv.ayush.myapplication.BottomScreens.Library
import ggv.ayush.myapplication.DrawerScreens.AccountView
import ggv.ayush.myapplication.DrawerScreens.LogoutDialog
import ggv.ayush.myapplication.DrawerScreens.Subscription
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage
import ggv.ayush.myapplication.MainViewModel
import ggv.ayush.myapplication.Screen
import ggv.ayush.myapplication.Screen.BottomScreen.Home.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ggv.ayush.myapplication.R
import ggv.ayush.myapplication.Screen.BottomScreen.Browse.screensInBottom

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable

fun MainView(navController: NavHostController) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember { mutableStateOf(false) }

    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    // Allow us to find out on which "View" we current are
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dialogOpen = remember {
        mutableStateOf(false)
    }
    val navController = rememberNavController()

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val modalSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp



    //Bottom Bar
    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(Modifier.wrapContentSize()) {
                screensInBottom.forEach { item ->
                    val isSelected = currentRoute == item.bRoute
                    Log.d(
                        "Navigation",
                        "Item: ${item.bTitle}, Current Route: $currentRoute, Is Selected: $isSelected"
                    )
                    val tint = if (isSelected) Color.White else Color.Black
                    BottomNavigationItem(
                        selected = currentRoute == item.bRoute,
                        onClick = {
                            navController.navigate(item.bRoute)
                            title.value = item.bTitle
                        },
                        icon = {

                            Icon(
                                tint = tint,
                                contentDescription = item.bTitle,
                                painter = painterResource(id = item.icon)
                            )
                        },
                        label = { Text(text = item.bTitle, color = tint) },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black

                    )
                }
            }
        }
    }

    //Top app bar
    val AppTopBar: @Composable () -> Unit ={
        TopAppBar(title = {
            Text(title.value)
            },
            actions = {
                IconButton(
                    onClick = {
                        scope.launch {
                            if (modalSheetState.isVisible)
                                modalSheetState.hide()
                            else
                                modalSheetState.show()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    // Open the drawer
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Menu"
                    )
                }
            }
        )

    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius),
        sheetContent = {
            MoreBottomSheet(modifier = modifier)
        }) {

        //Scaffold MAIN
        Scaffold(
            bottomBar = bottomBar,
            scaffoldState = scaffoldState,
            topBar = {
                AppTopBar()
            },
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screensInDrawer) { item ->
                        item?.let {
                            DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                if (item.dRoute == "logout") {
                                    dialogOpen.value = true
                                } else {
                                    navController.navigate(item.dRoute)
                                    title.value = item.dTitle
                                }
                            }
                        }
                    }
                }
            }
        ) {
            BottomNavGraph(navController = navController, pd = it)
            LogoutDialog(dialogOpen = dialogOpen , navController = navController)
        }
    }
}



@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreen,
    onDrawerItemClicked: () -> Unit
) {
    val background = if (selected) Color.DarkGray else Color.White
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.h5,
        )
    }
}

@Composable
fun MoreBottomSheet(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                MaterialTheme.colors.primarySurface
            )
    ) {
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_settings_24),
                    contentDescription = "Settings"
                )
                Text(text = "Settings",
                    fontSize = 20.sp,
                    color = Color.White)
            }
            Row(modifier = modifier.padding(16.dp)) {
                androidx.compose.material.Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_share_24),
                    contentDescription = "Share"
                )

                androidx.compose.material.Text(
                    text = "Share",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Row(modifier = modifier.padding(16.dp)) {
                androidx.compose.material.Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_help_green),
                    contentDescription = "Help"
                )
                androidx.compose.material.Text(text = "Help", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}
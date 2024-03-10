package ggv.ayush.myapplication

import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost


@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.BottomScreen.Home.route, builder = {


        composable("login_page", content = { LoginPage(navController = navController) })

        composable("register_page", content = { RegisterPage(navController = navController) })

        composable("reset_page", content = { ResetPage(navController = navController) })


        composable(Screen.BottomScreen.Home.bRoute) {
            MainView(navController)
        }
        composable(Screen.DrawerScreen.Account.route){
            MainView(navController)
        }
        composable(Screen.DrawerScreen.Orders.route){
            MainView(navController)
        }


    })

}
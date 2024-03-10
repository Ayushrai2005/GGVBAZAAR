package ggv.ayush.myapplication

import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ggv.ayush.myapplication.BottomScreens.Browse
import ggv.ayush.myapplication.BottomScreens.Home
import ggv.ayush.myapplication.BottomScreens.Library


@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController as NavHostController, "MainView") {


        composable("login_page", content = { LoginPage(navController = navController) })

        composable("register_page", content = { RegisterPage(navController = navController) })

        composable("reset_page", content = { ResetPage(navController = navController) })


        composable("MainView" , content = { MainView(navController = navController)})
        

        composable(Screen.BottomScreen.Home.bRoute) {
            Home()
        }
        composable(Screen.BottomScreen.Buy.bRoute) {
            Library()
        }
        composable(Screen.BottomScreen.Rent.bRoute) {
            Library()
        }
        composable(Screen.BottomScreen.Cart.bRoute) {
           Browse()
        }
        
        composable(Screen.DrawerScreen.Account.route){
            MainView(navController)
        }
        composable(Screen.DrawerScreen.Orders.route){
            MainView(navController)
        }



    }

}
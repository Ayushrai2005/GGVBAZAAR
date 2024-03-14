package ggv.ayush.myapplication

import Home
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ggv.ayush.myapplication.BottomScreens.ForRent
import ggv.ayush.myapplication.DrawerScreens.AccountView


@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, "login_page") {


        composable("login_page", content = { LoginPage(navController = navController) })

        composable("register_page", content = { RegisterPage(navController = navController) })

        composable("reset_page", content = { ResetPage(navController = navController) })


        composable("Main_View"){
            MainView(navController = navController)
        }

        //Drawer Items
        composable(Screen.DrawerScreen.Account.route){
            AccountView()
        }

        //Bottom Bar Items
        composable(Screen.BottomScreen.Home.bRoute) {
            Home(navController)
        }

        composable(Screen.BottomScreen.Rent.bRoute) {
            ForRent(navController)
        }
        composable(Screen.BottomScreen.Cart.bRoute) {
            Home(navController)
        }



    }

}
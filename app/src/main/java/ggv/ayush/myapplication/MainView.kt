package ggv.ayush.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.LOGINSIGNUP.RegisterPage
import ggv.ayush.myapplication.LOGINSIGNUP.ResetPage

@Composable
fun MainView(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_page", builder = {



        composable("login_page", content = { LoginPage(navController = navController) })

        composable("register_page", content = { RegisterPage(navController = navController) })

        composable("reset_page", content = { ResetPage(navController = navController) })


        composable(Screen.BottomScreen.Home.bRoute){
            HomePage(navController)
        }


    })
}
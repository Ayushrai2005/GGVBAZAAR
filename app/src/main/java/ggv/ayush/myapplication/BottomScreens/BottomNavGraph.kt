package ggv.ayush.myapplication.BottomScreens

import Home
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ggv.ayush.myapplication.DrawerScreens.AccountView
import ggv.ayush.myapplication.DrawerScreens.ProductForm
import ggv.ayush.myapplication.DrawerScreens.Subscription
import ggv.ayush.myapplication.LOGINSIGNUP.LoginPage
import ggv.ayush.myapplication.MainView
import ggv.ayush.myapplication.Screen

@Composable
fun BottomNavGraph(navController: NavHostController ,pd: PaddingValues){

    NavHost(navController = navController,
        startDestination = Screen.BottomScreen.Home.route , modifier = Modifier.padding(pd)
    ){
        composable(route =  Screen.BottomScreen.Home.route){
            Home()
        }
        composable(route =  Screen.BottomScreen.Buy.route){
            Home()
        }
        composable(route =  Screen.BottomScreen.Rent.route){
            Library()
        }
        composable(route =  Screen.BottomScreen.Cart.route){
            Library()
        }

        composable("login_page", content = { LoginPage(navController = navController) })


        composable("Main_View"){
            MainView(navController = navController)
        }


        //Drawer Items
        composable(Screen.DrawerScreen.Account.route){
            AccountView()
        }

        composable(Screen.DrawerScreen.Orders.route){
            Subscription()
        }

        composable(Screen.DrawerScreen.SellOrRent.route){
            ProductForm()
        }


    }

}
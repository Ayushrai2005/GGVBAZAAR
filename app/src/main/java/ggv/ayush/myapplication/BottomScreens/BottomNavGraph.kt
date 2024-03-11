package ggv.ayush.myapplication.BottomScreens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ggv.ayush.myapplication.Screen

@Composable
fun BottomNavGraph(navController: NavHostController){

    NavHost(navController = navController,
        startDestination = Screen.BottomScreen.Home.route
    ){
        composable(route =  Screen.BottomScreen.Home.route){
            Home()
        }
        composable(route =  Screen.BottomScreen.Buy.route){
            Browse()
        }
        composable(route =  Screen.BottomScreen.Rent.route){
            Library()
        }
        composable(route =  Screen.BottomScreen.Cart.route){
            Browse()
        }


    }

}
package ggv.ayush.myapplication

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(bTitle, bRoute) {
        object Home : BottomScreen("Home", "home", R.drawable.baseline_home_24)

    }

    sealed class DrawerScreen(
        val dTitle: String,
        val dRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(dTitle, dRoute) {
        object Account : DrawerScreen(
            "Account",
            "account",
            R.drawable.baseline_account_box_24
        )

        object Orders : DrawerScreen(
            "Orders",
            "orders",
            R.drawable.ic_filled_library_books_24
        )

        object Logout:DrawerScreen(
            "Logout",
            "logout",
            R.drawable.ic_filled_logout
        )



    }

    val screensInDrawer = listOf(
        Screen.DrawerScreen.Account,
        Screen.DrawerScreen.Orders,
        Screen.DrawerScreen.Logout
    )



}


package ggv.ayush.myapplication

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(bTitle, bRoute) {

        object Home : BottomScreen(
            "Home",
            "home",
            R.drawable.baseline_home_24
        )

        object Buy : BottomScreen(
            "Buy",
            "buy",
            R.drawable.baseline_home_24
        )

        object Rent : BottomScreen(
            "Rent",
            "rent",
            R.drawable.baseline_home_24
        )

        object Cart : BottomScreen(
            "Cart",
            "cart",
            R.drawable.ic_outlined_shopping_cart_24
        )

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

        object SellOrRent:DrawerScreen(
            "Sell / Rent",
            "sellrent",
            R.drawable.ic_filled_logout
        )





    }

    val screensInDrawer = listOf(
        Screen.DrawerScreen.Account,
        Screen.DrawerScreen.Orders,
        Screen.DrawerScreen.Logout
    )

    val screensInBottom = listOf(
        Screen.BottomScreen.Home,
        Screen.BottomScreen.Buy,
        Screen.BottomScreen.Rent,
        Screen.BottomScreen.Cart
        )





}


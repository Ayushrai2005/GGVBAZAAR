package ggv.ayush.myapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ggv.ayush.myapplication.LOGINSIGNUP.User

class MainViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Home)

//    private val _CurrentUser : MutableState<User> = mutableStateOf("")
    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen:Screen){
        _currentScreen.value = screen
    }
}
package ggv.ayush.myapplication.BottomScreens

import androidx.annotation.DrawableRes
import ggv.ayush.myapplication.R

data class Lib(@DrawableRes val icon: Int, val name:String)

val libraries = listOf<Lib>(
    Lib(R.drawable.ic_playlist_green, "Playlist"),
    Lib(R.drawable.ic_microphone,"Artists"),
)
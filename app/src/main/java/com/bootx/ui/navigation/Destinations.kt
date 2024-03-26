package com.bootx.ui.navigation

sealed class Destinations(val route: String) {
    //首页
    data object MainFrame : Destinations("MainFrame")
    //播放页
    data object PlayFrame : Destinations("PlayFrame")
}

package com.wfprogramin.mvvmcomposewr.navigation

sealed class AppScreens(val route: String) {
    data object FirstScreen: AppScreens("login")
    data object SecondScreen: AppScreens("second_screen")
}
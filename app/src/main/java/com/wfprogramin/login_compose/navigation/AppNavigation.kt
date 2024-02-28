package com.wfprogramin.login_compose.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wfprogramin.login_compose.ui.login.ui.view.Login
import com.wfprogramin.mvvmcomposewr.navigation.AppScreens

@Composable
fun AppNavigation(uri: Uri) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.FirstScreen.route){
        composable(route = AppScreens.FirstScreen.route){
            Login(uri)
        }
//        composable(route = AppScreens.SecondScreen.route){
//            WelcomeScreenTwo(navController)
//        }
    }
}
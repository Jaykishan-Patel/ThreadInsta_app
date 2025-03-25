package com.example.threadinsta.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadinsta.navigation.Routes.OtherUser
import com.example.threadinsta.screen.AddThreads
import com.example.threadinsta.screen.BottomNav
import com.example.threadinsta.screen.Home
import com.example.threadinsta.screen.Login
import com.example.threadinsta.screen.Register
import com.example.threadinsta.screen.Notification
import com.example.threadinsta.screen.OtherUser
import com.example.threadinsta.screen.Profile
import com.example.threadinsta.screen.Search
import com.example.threadinsta.screen.Splash

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            Splash(navController)
        }
        composable(Routes.Home.route) {
            Home(navController)
        }
        composable(Routes.Search.route) {
            Search(navController)
        }
        composable(Routes.AddThreads.route) {
            AddThreads(navController)
        }
        composable(Routes.Notification.route) {
            Notification()
        }
        composable(Routes.Profile.route) {
            Profile(navController)
        }
        composable(Routes.BottomNav.route) {
            BottomNav(navController)
        }
        composable(Routes.Login.route) {
            Login(navController)
        }
        composable(Routes.Register.route) {
            Register(navController)
        }
        composable(Routes.OtherUser.route) {
            val userId = it.arguments?.getString("userId")
            if (userId != null) {
                OtherUser(navController, userId)
            }
        }
    }

}
package com.example.threadinsta.navigation

sealed class Routes(val route:String) {
    object Home:Routes("home")
    object Search:Routes("search")
    object AddThreads:Routes("add_threads")
    object Notification:Routes("notification")
    object Profile:Routes("profile")
    object Splash:Routes("splash")
    object BottomNav:Routes("bottom_nav")
    object Login:Routes("login")
    object Register:Routes("register")
    object OtherUser:Routes("other_user/{userId}")

}
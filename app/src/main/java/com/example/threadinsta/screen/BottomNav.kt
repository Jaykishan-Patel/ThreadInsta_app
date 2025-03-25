package com.example.threadinsta.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.threadinsta.model.BottomNewItem
import com.example.threadinsta.navigation.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNav(navController: NavHostController){
    val navController1= rememberNavController()
    Scaffold (bottomBar = { MyBottomBar(navController1) })
    {innerPadding->
        NavHost(navController = navController1, startDestination = Routes.Home.route,
            modifier= Modifier.padding(innerPadding)) {
            composable(Routes.Home.route) {
                Home(navController1)
            }
            composable(Routes.Search.route) {
                Search(navController1)
            }
            composable(Routes.AddThreads.route) {
                AddThreads(navController1)
            }
            composable(Routes.Notification.route) {
                Notification()

        }
            composable(Routes.Profile.route) {
                Profile(navController)
            }
            composable (Routes.OtherUser.route){
                val userId=it.arguments?.getString("userId")
                if (userId != null) {
                    OtherUser(navController,userId)
                }
            }
        }

    }
}
@Composable
fun MyBottomBar(navController1: NavHostController){
    val backStackEntry=navController1.currentBackStackEntryAsState()
    val list= listOf(
        BottomNewItem("Home", Routes.Home.route, Icons.Rounded.Home),
        BottomNewItem("Search", Routes.Search.route, Icons.Rounded.Search),
        BottomNewItem("AddThreads", Routes.AddThreads.route, Icons.Rounded.Add),
        BottomNewItem("Notification", Routes.Notification.route, Icons.Rounded.Notifications),
        BottomNewItem("Profile", Routes.Profile.route, Icons.Rounded.Person)
    )
    BottomAppBar(modifier = Modifier.height(100.dp)){
        list.forEach {
                item->
            val selected=item.route==backStackEntry.value?.destination?.route
            NavigationBarItem(selected = selected,
                onClick = {
                    navController1.navigate(item.route) {
                        popUpTo(navController1.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon ={
                    Icon(item.icon, contentDescription = item.title,)
                }
            )
        }
    }

}
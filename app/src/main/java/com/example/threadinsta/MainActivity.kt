package com.example.threadinsta

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.threadinsta.navigation.NavGraph
import com.example.threadinsta.ui.theme.ThreadInstaTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThreadInstaTheme {
                val navController = rememberNavController()
                Column (modifier = Modifier.fillMaxSize()) {
                    NavGraph(navController)
                }
            }
        }
    }
}


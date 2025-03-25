package com.example.threadinsta.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadinsta.itemView.ThreadItem
import com.example.threadinsta.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavHostController){

   val homeViewModel:HomeViewModel= viewModel()
    val threadsAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)


    if (threadsAndUsers==null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center  // Centers both horizontally & vertically
        ) {
            Column {
                Text(
                    "It takes more than usual time, please check your internet connection",
                    modifier = Modifier.padding(6.dp),  // Remove fillMaxSize() to allow centering
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic
                )
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

        }

    }
    else{
        LazyColumn {
            items(threadsAndUsers?: emptyList()){
                    pair->
                ThreadItem(threads = pair.first, users = pair.second, navController =navController, userId = FirebaseAuth.getInstance().currentUser!!.uid)
            }
        }
    }





}


package com.example.threadinsta.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.threadinsta.R
import com.example.threadinsta.itemView.ThreadItem
import com.example.threadinsta.model.UserModel
import com.example.threadinsta.navigation.Routes
import com.example.threadinsta.utils.SharedPref
import com.example.threadinsta.viewmodel.AuthViewModel
import com.example.threadinsta.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navController: NavHostController){
    val context=LocalContext.current
    val viewModel:AuthViewModel= viewModel()
    val firebaseUser by viewModel.firebaseUser.observeAsState()
    val userViewModel:UserViewModel= viewModel()
    val threads by userViewModel.threads.observeAsState()

    if (firebaseUser!=null)
        userViewModel.fetchThread(firebaseUser!!.uid)
    val followers by userViewModel.followers.observeAsState(null)
    val following by userViewModel.following.observeAsState(null)
    if (firebaseUser!=null) {
        val uid = firebaseUser!!.uid
        userViewModel.getFollowers(uid)
        userViewModel.getFollowing(uid)
    }

    val user= UserModel(
        name = SharedPref.getName(context),
        imageUri = SharedPref.getImageUri(context)
    )

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Profile.route) {
                    inclusive = true
                }
            }
        }
    }



    Column(modifier=Modifier.fillMaxSize().padding(8.dp)) {
        Text("Profile",fontSize = 30.sp,
            modifier=Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column {

                Text(SharedPref.getName(context), modifier = Modifier.padding(4.dp),
               fontSize = 25.sp )
                Text(SharedPref.getUserName(context), modifier = Modifier.padding(3.dp),
                    fontSize = 20.sp)
                Spacer(modifier = Modifier.height(30.dp))
                Text(SharedPref.getBio(context), modifier = Modifier.padding(3.dp))
                Text("${followers?.size} Followers", modifier = Modifier.padding(top = 3.dp))
                Text("${following?.size} Following")
            }
          ImageWithPopup(SharedPref.getImageUri(context))

        }
        Button(onClick = {
            viewModel.logout()
        }) {
            Text("LOGOUT-->")
        }

        LazyColumn {
            items(threads?: emptyList()) {
                it->
                ThreadItem(threads = it, users = user, navController = navController, userId = SharedPref.getUserName(context))
            }
        }
    }

}

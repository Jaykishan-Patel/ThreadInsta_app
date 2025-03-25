package com.example.threadinsta.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.threadinsta.itemView.ThreadItem
import com.example.threadinsta.viewmodel.AuthViewModel
import com.example.threadinsta.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OtherUser(navController: NavHostController, userId: String) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
    val firebaseUser by viewModel.firebaseUser.observeAsState(null)
    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val user by userViewModel.users.observeAsState(null)
    val followers by userViewModel.followers.observeAsState(null)
    val following by userViewModel.following.observeAsState(null)

    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    LaunchedEffect(userId) {
        if(!userId.isEmpty()) {
            userViewModel.fetchThread(userId)
            userViewModel.fetchUser(userId)
            userViewModel.getFollowers(userId)
            userViewModel.getFollowing(userId)
        }
    }

    user?.let { userData ->
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Text(
                "Profile", fontSize = 40.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(userData.name ?: "No Name", modifier = Modifier.padding(4.dp), fontSize = 25.sp)
                    Text(userData.userName ?: "No Username", modifier = Modifier.padding(3.dp), fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(userData.bio ?: "No bio", modifier = Modifier.padding(3.dp))
                    Text("${followers?.size ?: 0} Followers", modifier = Modifier.padding(3.dp))
                    Text("${following?.size ?: 0} Following", modifier = Modifier.padding(3.dp))
                }
                ImageWithPopup(user!!.imageUri)
//                AsyncImage(model = user!!.imageUri,
//                    contentDescription = "userImage",
//                    modifier = Modifier.clip(CircleShape).size(90.dp),
//                    contentScale = ContentScale.Crop
//                )
            }

            Button(onClick = {
                if (currentUser.isNotEmpty() && followers?.contains(currentUser) == true) {
                    userViewModel.unFollowUsers(userId, currentUser)
                } else if (currentUser.isNotEmpty()) {
                    userViewModel.followUsers(userId, currentUser)
                }
            }) {
                Text(if (followers?.contains(currentUser) == true) "UnFollow" else "Follow")
            }

            LazyColumn {
                items(threads ?: emptyList()) { thread ->
                    ThreadItem(
                        threads = thread,
                        users = userData,
                        navController = navController,
                        userId = currentUser
                    )
                }
            }
        }
    }
}

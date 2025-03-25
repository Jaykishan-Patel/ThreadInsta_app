package com.example.threadinsta.itemView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.threadinsta.model.UserModel

@Composable
fun UserItem(
    users: UserModel,
    navController: NavHostController,
){
    Column (modifier = Modifier.fillMaxWidth()
        .padding(vertical = 5.dp, horizontal = 8.dp).clickable {
        navController.navigate("other_user/${users.uid}")
        }){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)) {
                AsyncImage(model = users.imageUri,
                    //rememberAsyncImagePainter(model = users.imageUri),
                    contentDescription = "user image",
                    modifier = Modifier.clip(CircleShape)
                        .size(45.dp),
                    contentScale = ContentScale.Crop)
             Column {

                 Text(users.name, fontSize = 20.sp,
                     modifier = Modifier.padding(start = 5.dp))
                 Text(users.userName,
                     modifier = Modifier.padding(start = 5.dp))
             }
             }
        Divider(color = Color.Gray,thickness = 1.dp)



    }
}


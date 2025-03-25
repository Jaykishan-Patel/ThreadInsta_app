package com.example.threadinsta.itemView

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.threadinsta.model.ThreadModel
import com.example.threadinsta.model.UserModel
import com.example.threadinsta.screen.ImageWithPopup
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ThreadItem(
    threads: ThreadModel,
    users: UserModel,
    navController: NavHostController,
    userId:String
){
    val timestamp =threads.timeStamp.toLong()
    val instant = Instant.ofEpochMilli(timestamp) // Convert to Instant
    val zonedDateTime = instant.atZone(ZoneId.systemDefault()) // Convert to local timezone

    // Extract Date
    val date = zonedDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    // Extract Time
    val time = zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    Column (modifier = Modifier.fillMaxWidth()
        .padding(8.dp)){

            Row(modifier = Modifier.padding(5.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ImageWithPopup(imageUrl = users.imageUri)
//                 AsyncImage(model = users.imageUri,
//                    contentDescription = "user image",
//                    modifier = Modifier.clip(CircleShape)
//                        .size(45.dp),
//                    contentScale = ContentScale.Crop)

                    Text(users.name, fontSize = 20.sp,
                        modifier = Modifier.padding(start = 5.dp).clickable{
                            navController.navigate("other_user/${users.uid}")
                        })
                }
                Column (modifier = Modifier.padding(1.dp)){
                    Text(time, fontSize = 12.sp)
                    Text(date, fontSize = 12.sp)
                }
            }

        ExpandableText(threads.thread)
//        Text(threads.thread,
//            modifier = Modifier.padding(5.dp))
//        Spacer(modifier = Modifier.padding(5.dp))
        if(threads.imageUri.isNotEmpty()){

            AsyncImage(
                model = threads.imageUri,
                contentDescription = "Loaded Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )


        }
        Divider(color = Color.Gray,thickness = 1.dp)




    }

}
@Composable
fun ExpandableText(text: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else 10,  // Show 10 lines initially
            overflow = TextOverflow.Ellipsis,  // Show "..." if text is truncated
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (text.length > 200) {  // Show "More" button only if text is long
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.End) // Align to bottom end
            ) {
                Text(if (expanded) "Less" else "More")
            }
        }
    }
}






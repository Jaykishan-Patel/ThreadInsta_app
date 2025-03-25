package com.example.threadinsta.screen


import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.threadinsta.R
import com.example.threadinsta.navigation.Routes
import com.example.threadinsta.utils.SharedPref
import com.example.threadinsta.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AddThreads(navController: NavHostController){
 val viewModel:AddThreadViewModel= viewModel()
 val isPosted by viewModel.isPosted.observeAsState(false)

 val context=LocalContext.current
var thread by remember { mutableStateOf("") }
 var imageUri by remember { mutableStateOf<Uri?>(null) }
 val permissionToRequest=if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
  Manifest.permission.READ_MEDIA_IMAGES
 }else Manifest.permission.READ_EXTERNAL_STORAGE
 val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
   uri: Uri? ->
  imageUri=uri
 }
 val permissionLauncher=rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){
   granted:Boolean->
  if (granted){
   Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
  }else{
   Toast.makeText(context,"Permission Denied. please allow permission for photo upload",Toast.LENGTH_SHORT).show()
  }
 }

 LaunchedEffect(isPosted) {
  if (isPosted){
   thread=""
   imageUri=null
   Toast.makeText(context,"Thread posted successfully",Toast.LENGTH_SHORT).show()
   navController.navigate(Routes.Home.route){
    popUpTo(Routes.AddThreads.route){
     inclusive=true
    }
   }
  }
 }

 ConstraintLayout(modifier = Modifier
  .fillMaxSize()
  .padding(16.dp)) {
  val(crossPic,text,logo,userName,editText,attachMedia,replyText,button,imageBox) = createRefs()
  Image(painter = painterResource(R.drawable.baseline_close_24),
   contentDescription = "close thread",
   colorFilter = ColorFilter.tint(Color.Red   ),
   modifier = Modifier
    .constrainAs(crossPic) {
     top.linkTo(parent.top)
     start.linkTo(parent.start)
    }
    .clickable {
     navController.navigate(Routes.Home.route){
      popUpTo(Routes.AddThreads.route){
       inclusive=true
      }
     }
    })
  Text(text = "Add Threads", style = TextStyle(
   fontWeight = FontWeight.ExtraBold,
   fontSize = 20.sp),
   modifier = Modifier.constrainAs(text) {
    top.linkTo(crossPic.top)
    start.linkTo(crossPic.end, margin = 10.dp)
    bottom.linkTo(crossPic.bottom)
   })
  AsyncImage(model = SharedPref.getImageUri(context),
   contentDescription = "logo",
   modifier = Modifier
    .constrainAs(logo) {
     top.linkTo(text.bottom, margin = 10.dp)
     start.linkTo(parent.start)
    }
    .size(45.dp)
    .clip(CircleShape),
   contentScale = ContentScale.Crop)

  Text(text = SharedPref.getUserName(context),
   style = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 15.sp),
modifier = Modifier.constrainAs(userName){
 top.linkTo(logo.top)
 start.linkTo(logo.end, margin = 8.dp)
 bottom.linkTo(logo.bottom)
})

  BasicTextFieldWithHint(hint = "Add a Thread",value = thread, onValueChange = {
   thread=it}, modifier = Modifier.constrainAs(editText){
    top.linkTo(logo.bottom, margin = 10.dp)
   start.linkTo(parent.start)
  })

  if (imageUri==null){
   Image(painter = painterResource(R.drawable.baseline_attachment_24),
    contentDescription = "attachMedia",
    colorFilter = ColorFilter.tint(Color.Cyan),
    modifier = Modifier.clickable{
     val isGranted= ContextCompat.checkSelfPermission(context,permissionToRequest)== PackageManager.PERMISSION_GRANTED
     if (isGranted){
      launcher.launch("image/*")
     }else{
      permissionLauncher.launch(permissionToRequest)
     }
    }.constrainAs(attachMedia){
     top.linkTo(editText.bottom, margin = 10.dp)
     start.linkTo(editText.start)
    }.size(50.dp))
  }
  else{
   Box (modifier=Modifier.background(Color.LightGray)
    .padding(1.dp).height(250.dp).fillMaxWidth()
    .constrainAs (imageBox){
     top.linkTo(editText.bottom, margin = 10.dp)
     start.linkTo(editText.start)
     end.linkTo(parent.end)
    }){
    AsyncImage(model =imageUri,
     contentDescription = "logo",
     contentScale = ContentScale.Crop,
     modifier = Modifier.fillMaxWidth().fillMaxHeight()
      )
    Icon(imageVector = Icons.Default.Close, contentDescription = "close",
     modifier=Modifier.align(Alignment.TopEnd).clickable {
      imageUri=null
     })
   }
  }

  Text("Anyone can reply",
   fontSize = 15.sp,
   modifier = Modifier.constrainAs(replyText){
    start.linkTo(parent.start, margin = 5.dp)
    bottom.linkTo(parent.bottom, margin = 12.dp)
   }
   )

  TextButton(onClick = {
   if(thread.isNotEmpty()){
    if(imageUri==null){
     viewModel.saveData(thread, FirebaseAuth.getInstance().currentUser!!.uid,"")
    }else{
     viewModel.saveImage(thread,FirebaseAuth.getInstance().currentUser!!.uid,imageUri!!,context)
    }
   }
   else{
    Toast.makeText(context,"Please add a thread",Toast.LENGTH_SHORT).show()
   }
  },
   modifier = Modifier.constrainAs(button){
    bottom.linkTo(parent.bottom)
    end.linkTo(parent.end, margin = 5.dp)
   } ){
   Text("Post", fontSize = 15.sp)
  }

 }

}

@Composable
fun BasicTextFieldWithHint(hint: String,value:String, onValueChange: (String) -> Unit,modifier: Modifier){
 Box(modifier = modifier) {
  OutlinedTextField(value, onValueChange,
   label = { Text(hint) },
   modifier = Modifier.fillMaxWidth())

 }

}


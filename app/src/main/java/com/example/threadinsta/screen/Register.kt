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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.threadinsta.R
import com.example.threadinsta.navigation.Routes
import com.example.threadinsta.viewmodel.AuthViewModel

@Composable
fun Register(navController: NavHostController){
    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel:AuthViewModel= viewModel()
    val firebaseUser by viewModel.firebaseUser.observeAsState()
    val context= LocalContext.current
    val permissionToRequest=if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    }else Manifest.permission.READ_EXTERNAL_STORAGE

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        uri: Uri? ->
        imageUri=uri
    }
    val permissionLauncher=rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){
        isgranted:Boolean->
        if (isgranted){
              Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
        }else{
              Toast.makeText(context,"Permission Denied. please allow permission for photo upload",Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.route) {
                popUpTo(Routes.Register.route) {
                    inclusive = true
                }
            }
        }
    }


    Column(modifier = Modifier.padding(24.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Register here", style = TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold)
        )

        Image(painter =if (imageUri==null) painterResource(R.drawable.person)else{
            rememberAsyncImagePainter(imageUri)
        }, contentDescription = "person",
            modifier = Modifier.size(96.dp).clip(CircleShape).background(color = Color.LightGray).clickable{
                val isGranted= ContextCompat.checkSelfPermission(context,permissionToRequest)== PackageManager.PERMISSION_GRANTED
                if (isGranted){
                    launcher.launch("image/*")
                }else{
                    permissionLauncher.launch(permissionToRequest)
                }
            })

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = name,
            onValueChange = {name=it},
            label = { Text(text = "Name")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(value = userName,
            onValueChange = {userName=it},
            label = { Text(text = "User Name")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

        OutlinedTextField(value = bio,
            onValueChange = {bio=it},
            label = { Text(text = "Bio")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

        OutlinedTextField(value = email,
            onValueChange = {email=it},
            label = { Text(text = "Email")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

        OutlinedTextField(value = password,
            onValueChange = {password=it},
            label = { Text(text = "Password")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(onClick = {
            if(imageUri==null || name.isEmpty() || userName.isEmpty() || bio.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(context,"Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.register(email,password,name,userName,bio,imageUri!!,context)
            }


        }) {
            Text(text = "Register",
                style = TextStyle(fontSize = 20.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = {
            navController.navigate(Routes.Login.route){
                popUpTo(Routes.Register.route){
                    inclusive=true
                }
            }
        }) {
            Text(text = "Have a Account? go to Login page -->",
                style = TextStyle(fontSize = 20.sp,
                    textAlign = TextAlign.Center)
            )
        }


    }
}






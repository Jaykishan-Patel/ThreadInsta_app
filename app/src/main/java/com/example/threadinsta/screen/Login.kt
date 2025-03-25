package com.example.threadinsta.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadinsta.navigation.Routes
import com.example.threadinsta.viewmodel.AuthViewModel

@Composable
fun Login(navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel:AuthViewModel= viewModel()
    val firebaseUser by viewModel.firebaseUser.observeAsState()
    val context=LocalContext.current
    val error by viewModel.error.observeAsState()

    LaunchedEffect(firebaseUser){
        if (firebaseUser!=null){
            navController.navigate(Routes.BottomNav.route){
                popUpTo(Routes.Login.route)
            }
        }

    }
    LaunchedEffect(error){
        if (error!=null){
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.padding(24.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Login", style = TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = email,
            onValueChange = {email=it},
            label = { Text(text = "Email")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(value = password,
            onValueChange = {password=it},
            label = { Text(text = "Password")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(onClick = {
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(context,"Please fill all fields",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.login(email,password,context)
            }

        }) {
            Text(text = "Login",
                style = TextStyle(fontSize = 20.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = {
            navController.navigate(Routes.Register.route){
                popUpTo(Routes.Login.route){
                    inclusive=true
                }
            }
        }) {
            Text(text = "New User? Create Account -->",
                style = TextStyle(fontSize = 20.sp,
                textAlign = TextAlign.Center))
        }


    }
}




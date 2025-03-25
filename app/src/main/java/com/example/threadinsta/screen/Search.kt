package com.example.threadinsta.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.threadinsta.itemView.UserItem
import com.example.threadinsta.viewmodel.SearchViewModel

@Composable
fun Search(navController: NavHostController){
    val searchViewModel: SearchViewModel=viewModel ()
    val users by searchViewModel.userList.observeAsState()
    var search by remember { mutableStateOf("") }

    Column {
        Text("Search", fontSize = 34.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = search,
            onValueChange = {
                search=it
            },
            label = {Text("search user")}
            ,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search icon") }
        )
        LazyColumn {
            if(!users.isNullOrEmpty()){
                val filterUser=users!!.filter { it.name.contains(search,ignoreCase = true) }
                items (filterUser){
                        user->
                    UserItem(users = user, navController = navController)
                }
            }

        }
    }



}
package com.example.threadinsta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadinsta.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel:ViewModel() {
    private val db= FirebaseDatabase.getInstance()
    val users=db.getReference("users")
    private val _userList=MutableLiveData<List<UserModel>>()
    val userList:LiveData<List<UserModel>> get() =_userList
    init {
        fetchUsers {
            _userList.value=it
        }
    }

    private fun fetchUsers(onResult:(List<UserModel>)->Unit){
        users.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result=mutableListOf<UserModel>()
                for(userSnapShot in snapshot.children){
                    val user=userSnapShot.getValue<UserModel>(UserModel::class.java)
                    result.add(user!!)
                }
                onResult(result)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
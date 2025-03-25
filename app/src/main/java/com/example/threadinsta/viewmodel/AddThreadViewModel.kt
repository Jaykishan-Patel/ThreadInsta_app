package com.example.threadinsta.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadinsta.model.ThreadModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AddThreadViewModel : ViewModel(){
    private val db= FirebaseDatabase.getInstance()
    val usersRef=db.getReference("threads")

    private val storageRef= Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")


    private val _isPosted= MutableLiveData<Boolean>()
    val isPosted: MutableLiveData<Boolean> = _isPosted

    fun saveImage(
        thread: String,
        userId: String,
        imageUri:Uri,
        context:Context
    ){
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener { downloadUrl ->
                        Log.i("imageUri",downloadUrl.toString())
                        saveData(thread, userId, downloadUrl.toString())
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch image URL", Toast.LENGTH_SHORT).show()
                        _isPosted.postValue(false)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                _isPosted.postValue(false)
            }

    }

     fun saveData(thread: String, userId: String, imageUri:String) {
          val threadData=ThreadModel(thread,imageUri,userId, System.currentTimeMillis().toString())
        usersRef.child(usersRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
            }
    }
}
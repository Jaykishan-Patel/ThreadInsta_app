package com.example.threadinsta.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

object SharedPref {
    fun StoreData(
        name: String,
        email: String,
        bio: String,
        userName: String,
        imageUri: String,
        context: Context){
        val sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE)
        sharedPreferences.edit() {
            putString("name", name)
            putString("email", email)
            putString("bio", bio)
            putString("userName", userName)
            putString("imageUri", imageUri)
        }
    }
    fun getUserName(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("userName","")!!
    }
    fun getName(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("name","")!!
    }
    fun getEmail(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("email","")!!
    }
    fun getBio(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("bio","")!!
    }
    fun getImageUri(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("imageUri","")!!
    }

}
package com.example.threadinsta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadinsta.model.ThreadModel
import com.example.threadinsta.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel:ViewModel() {
    private val db= FirebaseDatabase.getInstance()
    private val threadRef=db.getReference("threads")
    private val userRef=db.getReference("users")

    private val _threads=MutableLiveData<List<ThreadModel>>()
    val threads: LiveData<List<ThreadModel>> get() = _threads

    private val _users=MutableLiveData<UserModel>()
    val users: LiveData<UserModel> get() = _users

    private val _followers=MutableLiveData<List<String>>()
    val followers:LiveData<List<String>> =_followers

    private val _following=MutableLiveData<List<String>>()
    val following:LiveData<List<String>> =_following


    fun fetchUser(uid:String){
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(UserModel::class.java)
                _users.postValue(user!!)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchThread(uid:String){
     threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener{
         override fun onDataChange(snapshot: DataSnapshot) {
             val threadList=snapshot.children.mapNotNull {
                 it.getValue(ThreadModel::class.java)
             }
             if (threadList.isNotEmpty()){
                 _threads.postValue(threadList)
             }
         }

         override fun onCancelled(p0: DatabaseError) {
             TODO("Not yet implemented")
         }

     })
    }
    val firestoreDb=Firebase.firestore

    fun followUsers(userId: String, currentUserId: String) {
        val ref = firestoreDb.collection("following").document(currentUserId)
        val followersRef = firestoreDb.collection("followers").document(userId)

        // Ensure the document exists before updating
        ref.set(mapOf("following" to FieldValue.arrayUnion(userId)), SetOptions.merge())
        followersRef.set(mapOf("followers" to FieldValue.arrayUnion(currentUserId)), SetOptions.merge())
    }


    fun getFollowers(userId: String){
        firestoreDb.collection("followers").document(userId).addSnapshotListener { value, error ->
            val followersId= (value?.get("followers") as? List<String>) ?: listOf()
            _followers.postValue(followersId)
        }
    }

    fun getFollowing(userId: String){
        firestoreDb.collection("following").document(userId).addSnapshotListener { value, error ->
            val followingId= (value?.get("following") as? List<String>) ?: listOf()
            _following.postValue(followingId)
        }
    }

    fun unFollowUsers(userId: String,currentUserId: String){
        val ref=firestoreDb.collection("following").document(currentUserId)
        val followersRef=firestoreDb.collection("followers").document(userId)
        ref.update("following",FieldValue.arrayRemove(userId))
        followersRef.update("followers",FieldValue.arrayRemove(currentUserId))
    }
}
package com.example.threadinsta.viewmodel
import com.google.firebase.database.ValueEventListener
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadinsta.model.UserModel
import com.example.threadinsta.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel: ViewModel() {
    val auth=FirebaseAuth.getInstance()
    private val db= FirebaseDatabase.getInstance()
    val usersRef=db.getReference("users")

    private val storageRef= Firebase.storage.reference
     val imagesRef=storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser= MutableLiveData<FirebaseUser?>(null)
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val _error= MutableLiveData<String>()
    val error: LiveData<String> = _error



    init {
        _firebaseUser.value=auth.currentUser
    }

    fun login(email:String,password:String,context: Context){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid,context)
               } else{
                    _error.postValue(it.exception?.message)
                }
            }
    }

    private fun getData(uid: String,context: Context) {
        usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.StoreData(userData!!.name,userData.email,userData.bio,userData.userName,userData.imageUri,context)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun register(email:String,password:String,name:String,userName:String,bio:String,imageUri:Uri,context: Context){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(name,userName,bio,imageUri,email,password,auth.currentUser?.uid,context)
                }else{

                    _error.postValue("something went wrong")
                }
            }
    }
    private fun saveImage(name: String,userName: String,bio: String,imageUri: Uri,email: String,password: String,uid: String?,context: Context) {
        val fireStoreDb= Firebase.firestore
        val followersRef=fireStoreDb.collection("followers").document(uid!!)
        val followingRef=fireStoreDb.collection("following").document(uid)

        followersRef.set(mapOf("followers" to listOf<String>()))
        followingRef.set(mapOf("following" to listOf<String>()))

        val uploadTask=imagesRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener {
                saveData(name,userName,bio,it.toString(),email,password,uid, context)
            }
        }
    }

    private fun saveData(name: String,userName: String,bio: String,imageUri: String,email: String,password: String,uid: String?,context: Context) {
        val userData=UserModel(name,userName,bio,imageUri,email,password,uid!!)
        usersRef.child(uid).setValue(userData)
            .addOnSuccessListener {
              SharedPref.StoreData(name,email,bio,userName,imageUri,context)
            }.addOnFailureListener {

            }
    }

    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }


}



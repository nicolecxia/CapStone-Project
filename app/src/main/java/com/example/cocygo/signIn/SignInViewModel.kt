package com.example.cocygo.signIn

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel:ViewModel() {
    private var firebaseAuth: FirebaseAuth =  FirebaseAuth.getInstance()

    // LiveData for observing authentication results
    val userEmail = MutableLiveData<String>()
    val signInFlag = MutableLiveData<Boolean>()
    val signInErrorMSG = MutableLiveData<String>()

    fun signIn(activity: Activity, email: String, pass:String){
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(activity) {  it ->
                    if (it.isSuccessful) {
                        userEmail.postValue(firebaseAuth.currentUser?.email.toString())
                        signInFlag.postValue(true)

                    } else {
                        signInFlag.postValue(false)
                        signInErrorMSG.postValue(it.exception.toString())
                    }
                }
        } else {
            signInFlag.value = false
            signInErrorMSG.value = "Empty fields are not allowed !"
        }
    }

    fun signOut(){
        firebaseAuth.signOut()

        signInFlag.postValue(false)
        userEmail.postValue(null)
    }

}
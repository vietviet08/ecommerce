package com.vietquoc.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.vietquoc.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _login = MutableSharedFlow<Resource<String>>()
    val login = _login.asSharedFlow()

    fun login(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

            }
            .addOnFailureListener{

            }
    }

}
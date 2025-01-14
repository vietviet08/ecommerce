package com.vietquoc.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vietquoc.ecommerce.data.User
import com.vietquoc.ecommerce.util.Constants
import com.vietquoc.ecommerce.util.RegisterFieldState
import com.vietquoc.ecommerce.util.RegisterValidation
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.util.validationEmail
import com.vietquoc.ecommerce.util.validationPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {

        if (validationEmailAndPassword(user.email, password)) {

            runBlocking {
                _register.emit(Resource.Loading())
            }

            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(user, userUid = it.uid)
                    }

                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(
                validationEmail(user.email),
                validationPassword(password)
            )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun validationEmailAndPassword(email: String, password: String): Boolean {
        val emailValidation = validationEmail(email)
        val passwordValidation = validationPassword(password)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }

    private fun saveUserInfo(user: User, userUid: String) {
        db.collection(Constants.USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }
}
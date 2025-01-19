package com.vietquoc.ecommerce.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vietquoc.ecommerce.BuildConfig
import com.vietquoc.ecommerce.EcommerceApplication
import com.vietquoc.ecommerce.data.User
import com.vietquoc.ecommerce.util.RegisterValidation
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.util.validationEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val cloudinary: Cloudinary,
    app: Application
) : AndroidViewModel(app) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUser()
    }

    fun updateUser(user: User, imageUri: Uri?) {
        val areInputsValid = validationEmail(user.email) is RegisterValidation.Success
                && user.firstName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()

        if (!areInputsValid) {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error("Check your inputs"))
            }
        }

        viewModelScope.launch {
            _updateInfo.emit(Resource.Loading())
        }

        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }
    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = withContext(Dispatchers.IO) {
                    MediaStore.Images.Media.getBitmap(
                        getApplication<EcommerceApplication>().contentResolver,
                        imageUri
                    )
                }

                val byteArray = withContext(Dispatchers.IO) {
                    ByteArrayOutputStream().use { byteArrayOutputStream ->
                        imageBitmap.compress(
                            android.graphics.Bitmap.CompressFormat.JPEG,
                            90,
                            byteArrayOutputStream
                        )
                        byteArrayOutputStream.toByteArray()
                    }
                }

                val options = mapOf(
                    "public_id" to "profile_image/${auth.uid}/${UUID.randomUUID()}",
                    "resource_type" to "image"
                )

                val uploadResult = withContext(Dispatchers.IO) {
                    cloudinary.uploader().upload(byteArray, options)
                }

                val imageUrl = uploadResult["secure_url"].toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)

            } catch (e: Exception) {
                viewModelScope.launch {
                    _updateInfo.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    private fun saveUserInformation(user: User, b: Boolean) {
        firestore.runTransaction { transaction ->
            val documentRef = firestore.collection("user").document(auth.uid!!)

            if (b) {
                val currentUser = transaction.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transaction.set(documentRef, newUser)
            } else {
                transaction.set(documentRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource.Loading())

        }

        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Resource.Success(it))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}
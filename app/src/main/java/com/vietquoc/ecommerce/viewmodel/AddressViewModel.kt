package com.vietquoc.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.vietquoc.ecommerce.data.Address
import com.vietquoc.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _editAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val editAddress = _editAddress.asStateFlow()

    private val _deleteAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val deleteAddress = _deleteAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            viewModelScope.launch {
                _addNewAddress.emit(Resource.Loading())

                firestore.collection("user").document(auth.uid!!).collection("address")
                    .document().set(address)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            _addNewAddress.emit(Resource.Success(address))
                        }
                    }
                    .addOnFailureListener {
                        viewModelScope.launch {
                            _addNewAddress.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        } else {
            viewModelScope.launch {
                _error.emit("All fields are required")
            }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() &&
                address.fullName.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty()
    }

    fun editAddress(address: Address) {
        val validateInputs = validateInputs(address)

        if (validateInputs) {
            viewModelScope.launch {
                try {
                    _editAddress.emit(Resource.Loading())

                    val querySnapshot = firestore.collection("user")
                        .document(auth.uid!!)
                        .collection("address")
                        .whereEqualTo("addressId", address.addressId)
                        .get().await()

                    val documentRef = querySnapshot.documents.first().reference

                    documentRef.set(address).await()

                    _editAddress.emit(Resource.Success(address))
                } catch (e: Exception) {
                    _editAddress.emit(Resource.Error(e.message ?: "Failed to edit address"))
                }
            }
        } else {
            viewModelScope.launch {
                _error.emit("All fields are required")
            }
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            try {
                _deleteAddress.emit(Resource.Loading())

                val querySnapshot = firestore.collection("user")
                    .document(auth.uid!!)
                    .collection("address")
                    .whereEqualTo("addressId", address.addressId)
                    .get().await()

                val documentRef = querySnapshot.documents.first().reference

                documentRef.delete().await()

                _deleteAddress.emit(Resource.Success(address))
            } catch (e: Exception) {
                _deleteAddress.emit(Resource.Error(e.message ?: "Failed to delete address"))
            }
        }
    }
}
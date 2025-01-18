package com.vietquoc.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.vietquoc.ecommerce.data.Product
import com.vietquoc.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts = _specialProducts.asStateFlow()

    private val _bestDealProducts =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealProducts = _bestDealProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    private val pageInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDealProduct()
        fetchBestProduct()
    }

    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Special Products").get()
            .addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestDealProduct() {
        viewModelScope.launch {
            _bestDealProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDealProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProducts.emit(Resource.Success(bestDealProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestDealProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProduct() {

        if (!pageInfo.isLast) {

            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }
            firestore.collection("Products").limit(pageInfo.bestProductPage * 10).get()
                .addOnSuccessListener { result ->
                    val bestProductsList = result.toObjects(Product::class.java)
                    pageInfo.isLast = bestProductsList == pageInfo.oldBestProducts
                    pageInfo.oldBestProducts = bestProductsList
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(bestProductsList))
                    }
                    pageInfo.bestProductPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

}

internal data class PagingInfo(
    var bestProductPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isLast: Boolean = false
)
package com.vietquoc.ecommerce.data.order

import com.google.firebase.Timestamp
import com.vietquoc.ecommerce.data.Address
import com.vietquoc.ecommerce.data.CartProduct

class Order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = Timestamp.now().toDate().toString(),
) {
}
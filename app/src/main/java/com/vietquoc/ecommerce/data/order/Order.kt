package com.vietquoc.ecommerce.data.order

import android.os.Parcelable
import com.vietquoc.ecommerce.data.Address
import com.vietquoc.ecommerce.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
    val orderId: Long = Random.nextLong(0, 100_000_000_000) + totalPrice.toLong()
) : Parcelable {
    constructor() : this("", 0f, emptyList())
}
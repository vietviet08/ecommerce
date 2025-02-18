package com.vietquoc.ecommerce.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Address(
    val addressTitle: String,
    val fullName: String,
    val street: String,
    val phone: String,
    val city: String,
    val state: String,
    val addressId: Long = Random.nextLong(0, Long.MAX_VALUE),
) : Parcelable {
    constructor() : this("", "", "", "", "", "")
}


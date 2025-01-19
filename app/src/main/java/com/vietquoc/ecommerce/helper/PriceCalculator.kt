package com.vietquoc.ecommerce.helper


fun Float?.getProductPrice(price: Float): Float {
    if (this == null)
        return price
    val priceAfterOffer = price - (price * this / 100)

    return priceAfterOffer
}
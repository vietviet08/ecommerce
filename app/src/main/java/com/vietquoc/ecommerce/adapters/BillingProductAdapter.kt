package com.vietquoc.ecommerce.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vietquoc.ecommerce.data.CartProduct
import com.vietquoc.ecommerce.databinding.BillingProductsRvItemBinding
import com.vietquoc.ecommerce.helper.getProductPrice

class BillingProductAdapter :
    RecyclerView.Adapter<BillingProductAdapter.BillingProductViewHolder>() {

    inner class BillingProductViewHolder(val binding: BillingProductsRvItemBinding) :
        ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvBillingProductQuantity.text = "x ${cartProduct.quantity}"
                tvProductCartPrice.text = "$ ${cartProduct.product.price}"

                val priceAfterPercentage =
                    cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterPercentage)}"

                tvCartProductSize.text = cartProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: 0
                    )
                )

            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(p0: CartProduct, p1: CartProduct): Boolean {
            return p0.product == p1.product
        }

        override fun areContentsTheSame(p0: CartProduct, p1: CartProduct): Boolean {
            return p0 == p1
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(p0.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: BillingProductViewHolder, p1: Int) {
        val billing = differ.currentList[p1]
        p0.bind(billing)
    }

}
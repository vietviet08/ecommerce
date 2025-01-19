package com.vietquoc.ecommerce.adapters

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vietquoc.ecommerce.data.CartProduct
import com.vietquoc.ecommerce.databinding.CartProductItemBinding

class CartProductAdapter :
    RecyclerView.Adapter<CartProductAdapter.CartProductsViewHolder>() {

    class CartProductsViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()

                cartProduct.product.offerPercentage?.let {
                    val priceAfterOffer =
                        cartProduct.product.price - (cartProduct.product.price * (it / 100))
                    tvProductCartPriceAfter.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    tvProductCartPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (cartProduct.product.offerPercentage == null) {
                    tvProductCartPrice.text =
                        "$ ${String.format("%.2f", cartProduct.product.price)}"
                    tvProductCartPriceAfter.visibility =
                        View.INVISIBLE
                }

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

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(p0: CartProduct, p1: CartProduct): Boolean {
            return p0.product.id == p1.product.id
        }

        override fun areContentsTheSame(p0: CartProduct, p1: CartProduct): Boolean {
            return p0 == p1
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CartProductsViewHolder {
        return CartProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(p0.context), p0, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: CartProductsViewHolder, p1: Int) {
        val cartProduct = differ.currentList[p1]
        p0.bind(cartProduct)

        p0.itemView.setOnClickListener {
            onClick?.invoke(cartProduct)
        }

        p0.binding.buttonPlusQuantity.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }

        p0.binding.buttonMinusQuantity.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }

    var onClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null
}
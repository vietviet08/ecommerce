package com.vietquoc.ecommerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vietquoc.ecommerce.data.Product
import com.vietquoc.ecommerce.databinding.BestDealsRvItemBinding
import com.vietquoc.ecommerce.databinding.ProductRvItemBinding

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {


    class BestProductViewHolder(private val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                product.offerPercentage?.let {
                    val priceAfterOffer = product.price - (product.price * (it / 100))
                    tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (product.offerPercentage == null) tvNewPrice.visibility = View.INVISIBLE

                tvPrice.text = "$ ${product.price}"
                tvName.text = product.name
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(p0: Product, p1: Product): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Product, p1: Product): Boolean {
            return p0 == p1
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(p0.context)
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: BestProductViewHolder, p1: Int) {
        val product = differ.currentList[p1]
        p0.bind(product)
    }
}
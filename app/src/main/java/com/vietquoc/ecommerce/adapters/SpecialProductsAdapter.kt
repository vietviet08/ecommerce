package com.vietquoc.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vietquoc.ecommerce.data.Product
import com.vietquoc.ecommerce.databinding.SpecialRvItemBinding

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgAd)
                tvAdName.text = product.name
                tvAdPrice.text = "$ ${product.price}"
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(p0: Product, p1: Product): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Product, p1: Product): Boolean {
            return p0 == p1
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(p0.context), p0, false
            )
        )
    }

    override fun getItemCount(): Int {
        val product = differ.currentList
        return product.size
    }

    override fun onBindViewHolder(p0: SpecialProductsViewHolder, p1: Int) {
        val product = differ.currentList[p1]
        p0.bind(product)
        p0.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    var onClick: ((Product) -> Unit)? = null
}
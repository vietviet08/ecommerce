package com.vietquoc.ecommerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vietquoc.ecommerce.databinding.ViewpagerImageItemBinding

class ViewPager2ImageAdapter :
    RecyclerView.Adapter<ViewPager2ImageAdapter.ViewPager2ImageViewHolder>() {
    class ViewPager2ImageViewHolder(val binding: ViewpagerImageItemBinding) :
        ViewHolder(binding.root) {
        fun bind(image: String) {
            Glide.with(itemView).load(image).into(binding.imageProductDetail)

        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(p0: String, p1: String): Boolean {
            return p0 == p1
        }

        override fun areContentsTheSame(p0: String, p1: String): Boolean {
            return p0 == p1
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewPager2ImageViewHolder {
        return ViewPager2ImageViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(p0.context), p0, false
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: ViewPager2ImageViewHolder, p1: Int) {
        val image = differ.currentList[p1]
        p0.bind(image)
    }


}
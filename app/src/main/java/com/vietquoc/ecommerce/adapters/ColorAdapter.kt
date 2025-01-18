package com.vietquoc.ecommerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vietquoc.ecommerce.databinding.ColorRvItemBinding

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition = -1

    inner class ColorViewHolder(val binding: ColorRvItemBinding) : ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {
            val imageDrawable = ColorDrawable(color)
            binding.imageShadowInside.setImageDrawable(imageDrawable)
            if (position == selectedPosition) {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE
                }
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
            return p0 == p1
        }

        override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
            return p0 == p1
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ColorViewHolder {
        return ColorViewHolder(
            ColorRvItemBinding.inflate(
                LayoutInflater.from(p0.context)
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: ColorViewHolder, p1: Int) {
        val color = differ.currentList[p1]
        p0.bind(color, p1)
        p0.itemView.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = p0.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }

    var onItemClick: ((Int) -> Unit)? = null
}
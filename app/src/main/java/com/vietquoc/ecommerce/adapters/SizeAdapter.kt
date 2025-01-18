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
import com.vietquoc.ecommerce.databinding.SizeRvItemBinding

class SizeAdapter : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    private var selectedPosition = -1

    inner class SizeViewHolder(val binding: SizeRvItemBinding) : ViewHolder(binding.root) {
        fun bind(size: String, position: Int) {
            binding.tvSize.text = size
            if (position == selectedPosition) {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    tvSize.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    tvSize.visibility = View.INVISIBLE
                }
            }
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

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SizeViewHolder {
        return SizeViewHolder(
            SizeRvItemBinding.inflate(
                LayoutInflater.from(p0.context)
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: SizeViewHolder, p1: Int) {
        val size = differ.currentList[p1]
        p0.bind(size, p1)
        p0.itemView.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = p0.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }

    var onItemClick: ((String) -> Unit)? = null
}
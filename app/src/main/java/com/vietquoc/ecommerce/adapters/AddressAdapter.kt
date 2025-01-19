package com.vietquoc.ecommerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.data.Address
import com.vietquoc.ecommerce.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressRvItemBinding) : ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if (isSelected) {
                    buttonAddress.background =
                        ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                } else {
                    buttonAddress.background =
                        ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                }
            }
        }

    }

    var selectedAddress = -1

    private val differCallback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(p0: Address, p1: Address): Boolean {
            return p0.addressTitle == p1.addressTitle && p0.fullName == p1.fullName
        }

        override fun areContentsTheSame(p0: Address, p1: Address): Boolean {
            return p0 == p1
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    init {
        differ.addListListener { _, _ ->
            notifyItemChanged(selectedAddress)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(p0.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: AddressViewHolder, p1: Int) {
        val address = differ.currentList[p1]
        p0.bind(address, selectedAddress == p1)
        p0.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = p0.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }
    }

    val onClick: ((Address) -> Unit)? = null
}
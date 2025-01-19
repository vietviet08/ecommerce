package com.vietquoc.ecommerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.data.order.Order
import com.vietquoc.ecommerce.data.order.OrderStatus
import com.vietquoc.ecommerce.data.order.getOrderStatus
import com.vietquoc.ecommerce.databinding.OrderItemBinding

class AllOrderAdapter : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    inner class AllOrderViewHolder(val binding: OrderItemBinding) : ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources
                val colorDrawable = when (getOrderStatus(order.orderStatus)) {
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }

                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }

                    is OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }

                    is OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }

                    is OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }

                    is OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(p0: Order, p1: Order): Boolean {
            return p0.products == p1.products
        }

        override fun areContentsTheSame(p0: Order, p1: Order): Boolean {
            return p0 == p1
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(p0.context),
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(p0: AllOrderViewHolder, p1: Int) {
        val order = differ.currentList[p1]
        p0.bind(order)
        p0.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    var onClick: ((Order) -> Unit)? = null
}
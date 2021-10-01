package ru.skillbranch.sbdelivery.ui.fragments.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Order
import ru.skillbranch.sbdelivery.extensions.millisToDate

class OrdersAdapter(val callback: Callback) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    private var items = listOf<Order>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<Order>) {
        val difCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(difCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvOrderItemDate: TextView = itemView.findViewById(R.id.tvOrderItemDate)
        private var tvOrderItemNumber: TextView = itemView.findViewById(R.id.tvOrderItemNumber)
        private var tvOrderItemPrice: TextView = itemView.findViewById(R.id.tvOrderItemPrice)
        private var tvOrderItemAddress: TextView = itemView.findViewById(R.id.tvOrderItemAddress)
        private var tvOrderItemStatus: TextView = itemView.findViewById(R.id.tvOrderItemStatus)

        fun bind(item: Order) {
            tvOrderItemDate.text = millisToDate(item.createdAt)
            tvOrderItemNumber.text = item.id
            (item.total.toString() + " ₽").also { tvOrderItemPrice.text = it }
            tvOrderItemAddress.text = item.address
            item.status.also { tvOrderItemStatus.text = it }

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    callback.onItemClicked(items[adapterPosition])
                }
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Order)
    }
}
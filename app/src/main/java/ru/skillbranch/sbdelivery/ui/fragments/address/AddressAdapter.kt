package ru.skillbranch.sbdelivery.ui.fragments.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.data.remote.res.toAddressString

class AddressAdapter(val callback: Callback) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    private var items = listOf<AddressRes>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.address_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<AddressRes>) {
        val difCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].city == data[newPos].city && items[oldPos].street == data[newPos].street && items[oldPos].house == data[newPos].house

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
        private var clItemAddress: ConstraintLayout = itemView.findViewById(R.id.clItemAddress)
        private var tvItemAddress: TextView = itemView.findViewById(R.id.tvItemAddress)

        fun bind(item: AddressRes) {
            item.toAddressString().also { tvItemAddress.text = it }

            clItemAddress.setOnClickListener {
                callback.onAddressClicked(item)
            }
        }
    }

    interface Callback {
        fun onAddressClicked(item: AddressRes)
    }
}
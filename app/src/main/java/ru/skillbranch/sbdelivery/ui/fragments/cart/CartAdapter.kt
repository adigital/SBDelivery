package ru.skillbranch.sbdelivery.ui.fragments.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.models.CartItemModel
import ru.skillbranch.sbdelivery.ui.custom.CounterView

class CartAdapter(val callback: Callback) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var items = listOf<CartItemModel>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<CartItemModel>) {
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
        private var rivCartItemPicture: RoundedImageView =
            itemView.findViewById(R.id.rivCartItemPicture)
        private var tvCartItemName: TextView = itemView.findViewById(R.id.tvCartItemName)
        private var cvCartItemCount: CounterView = itemView.findViewById(R.id.cvCartItemCount)
        private var tvCartItemPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)

        fun bind(item: CartItemModel) {
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.ic_baseline_more_horiz_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(rivCartItemPicture)

            tvCartItemName.text = item.name

            cvCartItemCount.setCounter(item.amount)
            cvCartItemCount.isZeroAllowed = true
            cvCartItemCount.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val itemChanged = CartItemModel(
                        item.name,
                        item.image,
                        cvCartItemCount.getCounter(),
                        item.price,
                        item.id
                    )

                    callback.onCounterClicked(itemChanged)
                }
            }

            tvCartItemPrice.text = (item.amount * item.price).toString().plus(" ₽")

            rivCartItemPicture.setOnClickListener {
                callback.onDishClicked(item)
            }

            tvCartItemName.setOnClickListener {
                callback.onDishClicked(item)
            }
        }
    }

    interface Callback {
        fun onCounterClicked(item: CartItemModel)
        fun onDishClicked(item: CartItemModel)
    }
}
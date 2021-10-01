package ru.skillbranch.sbdelivery.ui.fragments.home

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.skillbranch.sbdelivery.App.Companion.context
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Dish

class HomeAdapter(val callback: Callback) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var items = listOf<Dish>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<Dish>) {
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
        private var name: TextView = itemView.findViewById(R.id.tvDishItemName)
        private var picture: ImageView = itemView.findViewById(R.id.ivDishItemPicture)
        private var price: TextView = itemView.findViewById(R.id.tvDishItemPrice)
        private var action: TextView = itemView.findViewById(R.id.tvDishItemAction)
        private var favorites: ImageView = itemView.findViewById(R.id.ivDishItemFavorite)
        private var itemAdd: FloatingActionButton = itemView.findViewById(R.id.fabDishItemAdd)

        fun bind(item: Dish) {
            name.text = item.name
            price.text = item.price.toString().plus(" ₽")

            action.visibility = if (item.oldPrice != null) View.VISIBLE else View.GONE

            if (item.favorite) {
                favorites.imageTintList = (ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.dish_favorite_background
                    )
                ))
            } else {
                favorites.imageTintList =
                    (ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
            }

            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.ic_baseline_more_horiz_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(picture)

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    callback.onItemClicked(items[adapterPosition])
                }
            }

            favorites.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    callback.onItemFavoritesClicked(items[adapterPosition])
                }
            }

            itemAdd.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    callback.onInsertItemToCartClicked(items[adapterPosition])
                }
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Dish)
        fun onItemFavoritesClicked(item: Dish)
        fun onInsertItemToCartClicked(item: Dish)
    }
}
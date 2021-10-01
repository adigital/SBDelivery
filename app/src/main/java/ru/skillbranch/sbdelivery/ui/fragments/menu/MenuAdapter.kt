package ru.skillbranch.sbdelivery.ui.fragments.menu

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVGImageView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import ru.skillbranch.sbdelivery.App.Companion.context
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Category


class MenuAdapter(val callback: Callback) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var items = listOf<Category>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<Category>) {
        val difCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].categoryId == data[newPos].categoryId

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
        private var text: TextView = itemView.findViewById(R.id.tvMenuText)
        private var picture: SVGImageView = itemView.findViewById(R.id.svgivMenuPicture)

        fun bind(item: Category) {
            if (item.categoryId == "") {
                picture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
            }

            text.text = item.name

//            Glide.with(itemView.context)
//                .load(item.icon)
//                .placeholder(R.drawable.ic_baseline_more_horiz_24)
//                .error(R.drawable.ic_baseline_error_24)
//                .into(picture)

            if (item.icon != null) {
                GlideToVectorYou.justLoadImage(
                    itemView.context as Activity?,
                    Uri.parse(item.icon),
                    picture
                )

//                GlideToVectorYou
//                    .init()
//                    .with(itemView.context)
//                    .setPlaceHolder(
//                        R.drawable.ic_baseline_more_horiz_24,
//                        R.drawable.ic_baseline_error_24
//                    )
//                    .load(Uri.parse(item.icon), picture);

            }

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: Category)
    }
}
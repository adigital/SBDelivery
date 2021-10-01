package ru.skillbranch.sbdelivery.ui.fragments.notices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Notice

class NoticesAdapter :
    RecyclerView.Adapter<NoticesAdapter.NoticeViewHolder>() {

    private var items = listOf<Notice>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoticeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.notice_item, parent, false)
        )

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<Notice>) {
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

    inner class NoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var headerTextView: TextView? = itemView.findViewById(R.id.tvHeader)
        private val indicatorImageView = itemView.findViewById<ImageView>(R.id.ivIndicator)
        private var messageTextView: TextView? = itemView.findViewById(R.id.tvMessage)

        fun bind(item: Notice?) {
            headerTextView?.text = item?.header
            messageTextView?.text = item?.message

            if (item?.isNew == true) {
                headerTextView?.setTextColor(
                    ContextCompat.getColor(App.context, R.color.color_accent)
                )
                indicatorImageView.visibility = View.VISIBLE
            } else {
                headerTextView?.setTextColor(ContextCompat.getColor(App.context, R.color.white))
                indicatorImageView.visibility = View.INVISIBLE
            }
        }
    }
}
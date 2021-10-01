package ru.skillbranch.sbdelivery.ui.fragments.dish

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Review
import ru.skillbranch.sbdelivery.extensions.shortFormat
import java.text.SimpleDateFormat
import java.util.*

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    private var items = listOf<Review>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(data: List<Review>) {
        val difCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].dishId == data[newPos].dishId && items[oldPos].author == data[newPos].author && items[oldPos].date == data[newPos].date

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

        private var tvDishReviewNameDate: TextView =
            itemView.findViewById(R.id.tvDishReviewNameDate)
        private var rbDishReviewRating: RatingBar = itemView.findViewById(R.id.rbDishReviewRating)
        private var tvDishReviewComment: TextView = itemView.findViewById(R.id.tvDishReviewComment)

        fun bind(item: Review) {
            (item.author + ", " + SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(item.date)
                ?.shortFormat()).also { tvDishReviewNameDate.text = it }

            rbDishReviewRating.rating = item.rating

            tvDishReviewComment.visibility =
                if (item.text.isNullOrBlank()) View.GONE else View.VISIBLE
            tvDishReviewComment.text = item.text
        }
    }
}
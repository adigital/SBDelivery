package ru.skillbranch.sbdelivery.ui.fragments.dish

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.RootActivity
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.ui.custom.CounterView
import ru.skillbranch.sbdelivery.ui.dialogs.ReviewDialog
import ru.skillbranch.sbdelivery.viewmodels.fragments.DishViewModel
import kotlin.math.round

class DishFragment : Fragment() {

    private lateinit var viewModel: DishViewModel
    private lateinit var reviewsAdapter: ReviewsAdapter
    lateinit var item: Dish

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(DishViewModel::class.java)

        return inflater.inflate(R.layout.fragment_dish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey") { _, bundle ->
            val isOk = bundle.getBoolean("isOk")

            if (isOk) {
                viewModel.updateReviews(item.id)
            }
        }
    }

    private fun initView(view: View) {
        val args: DishFragmentArgs by navArgs()

        val tvDishName: TextView = view.findViewById(R.id.tvCartItemName)
        val tvDishDescription: TextView = view.findViewById(R.id.tvDishDescription)
        val tvDishPrice: TextView = view.findViewById(R.id.tvCartPrice)
        val tvDishOldPrice: TextView = view.findViewById(R.id.tvDishOldPrice)
        val ivDishPicture: ImageView = view.findViewById(R.id.ivDishPicture)
        val tvDishAction: TextView = view.findViewById(R.id.tvDishAction)
        val ivDishFavorite: ImageView = view.findViewById(R.id.ivDishFavorite)
        val cvDishCount: CounterView = view.findViewById(R.id.cvCartItemCount)
        val btnDishAddToCart: AppCompatButton = view.findViewById(R.id.btnDishAddToCart)
        val tvDishReviews: TextView = view.findViewById(R.id.tvDishReviews)
        val tvDishReviewsRating: TextView = view.findViewById(R.id.tvDishReviewsRating)
        val ivDishReviewsStar: ImageView = view.findViewById(R.id.ivDishReviewsStar)
        val rvDishReviews: RecyclerView = view.findViewById(R.id.rvDishReviews)
        val btnDishAddReview: AppCompatButton = view.findViewById(R.id.btnDishAddReview)

        (activity as RootActivity).supportActionBar?.title = args.name

        viewModel.getDishDao(args.dishId).observe(viewLifecycleOwner, { it ->
            item = it

            tvDishName.text = it.name
            tvDishDescription.text = it.description
            tvDishPrice.text = it.price.toString().plus(" ₽")

            if (it.oldPrice != null) {
                tvDishAction.visibility = View.VISIBLE
                tvDishOldPrice.paintFlags = tvDishOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvDishOldPrice.text = it.oldPrice.toString().plus(" ₽")
                tvDishOldPrice.visibility = View.VISIBLE
            } else {
                tvDishAction.visibility = View.GONE
                tvDishOldPrice.visibility = View.GONE
            }

            Glide.with(view.context)
                .load(it.image)
                .placeholder(R.drawable.ic_baseline_more_horiz_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(ivDishPicture)

            if (it.favorite) {
                ivDishFavorite.imageTintList = (ColorStateList.valueOf(
                    ContextCompat.getColor(
                        App.context,
                        R.color.dish_favorite_background
                    )
                ))
            } else {
                ivDishFavorite.imageTintList =
                    (ColorStateList.valueOf(ContextCompat.getColor(App.context, R.color.white)))
            }

            if (it.rating > 0) {
                tvDishReviews.text = getString(R.string.label_review)
                ivDishReviewsStar.visibility = View.VISIBLE
                tvDishReviewsRating.text = (round(it.rating * 100) / 100).toString() + "/5"
            } else {
                tvDishReviews.text = getString(R.string.label_no_review)
                ivDishReviewsStar.visibility = View.INVISIBLE
            }

            viewModel.updateReviews(it.id)

            viewModel.getReviews(it.id).observe(viewLifecycleOwner, {
                reviewsAdapter.updateData(it)
            })

        })

        ivDishFavorite.setOnClickListener {
            viewModel.updateFavoriteDish(item.id, !item.favorite)
        }

        var itemCount: Int

        btnDishAddToCart.setOnClickListener {
            itemCount = cvDishCount.getCounter()
            viewModel.insertItemToCart(item.id, itemCount, item.price)

            cvDishCount.setCounter(1)

            notifyShort("Блюдо \"${item.name}\" добавлено в корзину ($itemCount шт.)")
        }

        btnDishAddReview.setOnClickListener {
            viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
                if (!isAuth) {
                    findNavController().navigate(
                        DishFragmentDirections.actionNavDishToNavAuth("nav_dish")
                    )
                } else {
                    ReviewDialog().show(parentFragmentManager, item.id)
                }
            })
        }

        reviewsAdapter = ReviewsAdapter()
        rvDishReviews.adapter = reviewsAdapter
    }
}
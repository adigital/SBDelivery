package ru.skillbranch.sbdelivery.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.ui.custom.SpaceItemDecoration
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.viewmodels.fragments.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeRecommendedAdapter: HomeAdapter
    private lateinit var homeBestAdapter: HomeAdapter
    private lateinit var homeLikesAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.supportActionBar?.show()

        initView(view)
    }

    private fun initView(view: View) {
        viewModel.isDataSync.observe(viewLifecycleOwner, { isDataSync ->
            if (isDataSync) {
                val groupRecommended: Group = view.findViewById(R.id.group_recommended)
                val groupBest: Group = view.findViewById(R.id.group_best)
                val groupLikes: Group = view.findViewById(R.id.group_likes)

                viewModel.getRecommendedDishes().observe(viewLifecycleOwner, {
                    homeRecommendedAdapter.updateData(it)
                    groupRecommended.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                })

                viewModel.getBestDishes().observe(viewLifecycleOwner, {
                    homeBestAdapter.updateData(it)
                    groupBest.visibility = if (it.count() < 4) View.GONE else View.VISIBLE
                })

                viewModel.getLikesDishes().observe(viewLifecycleOwner, {
                    homeLikesAdapter.updateData(it)
                    groupLikes.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                })
            }
        })

        val recyclerViewRecommended: RecyclerView = view.findViewById(R.id.rv_recommended)
        val recyclerViewBest: RecyclerView = view.findViewById(R.id.rv_best)
        val recyclerViewLikes: RecyclerView = view.findViewById(R.id.rv_likes)

        homeRecommendedAdapter = HomeAdapter(object : HomeAdapter.Callback {
            override fun onItemClicked(item: Dish) {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavHomeToNavDish(item.id, item.name!!)
                )
            }

            override fun onItemFavoritesClicked(item: Dish) {
                updateFavoriteDish(item)
            }

            override fun onInsertItemToCartClicked(item: Dish) {
                insertItemToCart(item)
            }
        })
        recyclerViewRecommended.adapter = homeRecommendedAdapter

        recyclerViewRecommended.addItemDecoration(SpaceItemDecoration(8, 0))


        homeBestAdapter = HomeAdapter(object : HomeAdapter.Callback {
            override fun onItemClicked(item: Dish) {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavHomeToNavDish(item.id, item.name!!)
                )
            }

            override fun onItemFavoritesClicked(item: Dish) {
                updateFavoriteDish(item)
            }

            override fun onInsertItemToCartClicked(item: Dish) {
                insertItemToCart(item)
            }
        })
        recyclerViewBest.adapter = homeBestAdapter

        recyclerViewBest.addItemDecoration(SpaceItemDecoration(8, 0))


        homeLikesAdapter = HomeAdapter(object : HomeAdapter.Callback {
            override fun onItemClicked(item: Dish) {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavHomeToNavDish(item.id, item.name!!)
                )
            }

            override fun onItemFavoritesClicked(item: Dish) {
                updateFavoriteDish(item)
            }

            override fun onInsertItemToCartClicked(item: Dish) {
                insertItemToCart(item)
            }
        })
        recyclerViewLikes.adapter = homeLikesAdapter

        recyclerViewLikes.addItemDecoration(SpaceItemDecoration(8, 0))


        val tvHomeRecommendedAll: TextView = view.findViewById(R.id.tv_home_recommended_all)
        val tvHomeBestAll: TextView = view.findViewById(R.id.tv_home_best_all)
        val tvHomeLikesAll: TextView = view.findViewById(R.id.tv_home_likes_all)


        tvHomeBestAll.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToNavMenu()
            )
        }
        tvHomeRecommendedAll.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToNavMenu()
            )
        }
        tvHomeLikesAll.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavHomeToNavMenu()
            )
        }
    }

    private fun updateFavoriteDish(item: Dish) {
        viewModel.updateFavoriteDish(item.id, !item.favorite)
    }

    private fun insertItemToCart(item: Dish) {
        viewModel.insertItemToCart(item.id, 1, item.price)

        notifyShort("Блюдо \"${item.name}\" добавлено в корзину (1 шт.)")
    }
}
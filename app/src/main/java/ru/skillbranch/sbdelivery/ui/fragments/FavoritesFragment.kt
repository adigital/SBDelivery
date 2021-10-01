package ru.skillbranch.sbdelivery.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.ui.custom.SpaceItemDecoration
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.ui.fragments.home.HomeAdapter
import ru.skillbranch.sbdelivery.viewmodels.fragments.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    private fun initView(view: View) {
        val tvNoFavorites: TextView = view.findViewById(R.id.tvNoFavorites)
        val recyclerViewFavorites: RecyclerView = view.findViewById(R.id.rvFavorites)

        favoritesViewModel.getFavoriteDishes().observe(viewLifecycleOwner, {
            favoritesAdapter.updateData(it)

            if (it.isEmpty()) {
                tvNoFavorites.visibility = View.VISIBLE
                recyclerViewFavorites.visibility = View.GONE
            } else {
                tvNoFavorites.visibility = View.GONE
                recyclerViewFavorites.visibility = View.VISIBLE
            }
        })

        favoritesAdapter = HomeAdapter(object : HomeAdapter.Callback {
            override fun onItemClicked(item: Dish) {
                findNavController().navigate(
                    FavoritesFragmentDirections.actionNavFavoritesToNavDish(item.id, item.name!!)
                )
            }

            override fun onItemFavoritesClicked(item: Dish) {
                favoritesViewModel.updateFavoriteDish(item.id, !item.favorite)
            }

            override fun onInsertItemToCartClicked(item: Dish) {
                insertItemToCart(item)
            }
        })
        recyclerViewFavorites.adapter = favoritesAdapter

        recyclerViewFavorites.addItemDecoration(SpaceItemDecoration(13, 20))
    }

    private fun insertItemToCart(item: Dish) {
        favoritesViewModel.insertItemToCart(item.id, 1, item.price)

        notifyShort("Блюдо \"${item.name}\" добавлено в корзину (1 шт.)")
    }
}
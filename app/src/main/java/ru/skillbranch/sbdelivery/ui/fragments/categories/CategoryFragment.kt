package ru.skillbranch.sbdelivery.ui.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.ui.custom.SpaceItemDecoration
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.ui.fragments.home.HomeAdapter
import ru.skillbranch.sbdelivery.viewmodels.fragments.CategoriesViewModel
import ru.skillbranch.sbdelivery.viewmodels.fragments.CategoryViewModel

class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoriesViewModel: CategoriesViewModel

    private lateinit var categoryAdapter: HomeAdapter
    private var category: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoriesViewModel =
            ViewModelProvider(requireParentFragment()).get(CategoriesViewModel::class.java)

        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("categoryId") }?.apply {
            category = getString("categoryId")
            initView(view)
        }
    }

    private fun initView(view: View) {
        categoryViewModel.getDishesInCategory(category).observe(viewLifecycleOwner, {
            categoryAdapter.updateData(it)
        })

        categoriesViewModel.sort.observe(viewLifecycleOwner, MyObserver())

        val recyclerViewCategory: RecyclerView = view.findViewById(R.id.rvCategory)

        categoryAdapter = HomeAdapter(object : HomeAdapter.Callback {
            override fun onItemClicked(item: Dish) {
                findNavController().navigate(
                    CategoriesFragmentDirections.actionNavCategoriesToNavDish(item.id, item.name!!)
                )
            }

            override fun onInsertItemToCartClicked(item: Dish) {
                insertItemToCart(item)
            }

            override fun onItemFavoritesClicked(item: Dish) {
                categoryViewModel.updateFavoriteDish(item.id, !item.favorite)
            }
        })
        recyclerViewCategory.adapter = categoryAdapter

        recyclerViewCategory.addItemDecoration(SpaceItemDecoration(13, 20))
    }

    inner class MyObserver : Observer<CategoriesViewModel.Sort> {
        override fun onChanged(it: CategoriesViewModel.Sort) {
            categoryViewModel.setSortMode(it.itemNumber, it.isAscending)
        }
    }

    private fun insertItemToCart(item: Dish) {
        categoryViewModel.insertItemToCart(item.id, 1, item.price)

        notifyShort("Блюдо \"${item.name}\" добавлено в корзину (1 шт.)")
    }
}


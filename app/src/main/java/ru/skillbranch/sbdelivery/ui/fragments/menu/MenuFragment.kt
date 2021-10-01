package ru.skillbranch.sbdelivery.ui.fragments.menu

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Category
import ru.skillbranch.sbdelivery.data.local.entities.Dish
import ru.skillbranch.sbdelivery.data.local.entities.Search
import ru.skillbranch.sbdelivery.extensions.hideKeyboard
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.ui.fragments.home.HomeAdapter
import ru.skillbranch.sbdelivery.viewmodels.fragments.MenuViewModel


class MenuFragment : Fragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchResultAdapterCategories: SearchResultAdapterCategories
    private lateinit var searchResultAdapter: HomeAdapter
    private lateinit var navController: NavController
    private lateinit var recyclerViewMenu: RecyclerView
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var recyclerViewSearchResultCategories: RecyclerView
    private lateinit var recyclerViewSearchResult: RecyclerView
    private lateinit var groupSearchResult: Group
    private lateinit var searchView: SearchView
    private lateinit var scrollViewMenu: NestedScrollView
    private lateinit var searchQuery: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_menu, container, false)

        setHasOptionsMenu(true)

        initView(root)

        return root
    }

    private fun initView(view: View) {
        recyclerViewMenu = view.findViewById(R.id.rvMenu)
        recyclerViewSearch = view.findViewById(R.id.rvSearch)
        recyclerViewSearchResultCategories = view.findViewById(R.id.rvSearchResultCategories)
        recyclerViewSearchResult = view.findViewById(R.id.rvSearchResult)
        groupSearchResult = view.findViewById(R.id.groupSearchResult)
        scrollViewMenu = view.findViewById(R.id.svMenu)

        navController = NavHostFragment.findNavController(this)

        menuAdapter = MenuAdapter(object : MenuAdapter.Callback {
            override fun onItemClicked(item: Category) {
                findNavController().navigate(
                    MenuFragmentDirections.actionNavMenuToNavCategories(item.categoryId, item.name)
                )
            }
        })
        recyclerViewMenu.adapter = menuAdapter
        menuViewModel.getRootCategories()
            .observe(viewLifecycleOwner, { menuAdapter.updateData(it) })

        searchAdapter = SearchAdapter(object : SearchAdapter.Callback {
            override fun onItemClicked(item: Search) {
                searchView.setQuery(item.name, true)
            }

            override fun onItemDelete(item: Search) {
                menuViewModel.deleteSearchQuery(item.name)
            }
        })
        recyclerViewSearch.adapter = searchAdapter
        menuViewModel.getSearchQueries()
            .observe(viewLifecycleOwner, { searchAdapter.updateData(it) })

        searchResultAdapterCategories =
            SearchResultAdapterCategories(object : SearchResultAdapterCategories.Callback {
                override fun onItemClicked(item: Category) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionNavMenuToNavCategories(
                            item.categoryId,
                            item.name
                        )
                    )
                    menuViewModel.updateSearchQuery(searchQuery)
                }
            })
        recyclerViewSearchResultCategories.adapter = searchResultAdapterCategories
        menuViewModel.getSearchResultCategories("")
            .observe(viewLifecycleOwner, { searchResultAdapterCategories.updateData(it) })

        searchResultAdapter = HomeAdapter(object : HomeAdapter.Callback {
            override fun onItemClicked(item: Dish) {
                findNavController().navigate(
                    MenuFragmentDirections.actionNavMenuToNavDish(item.id, item.name!!)
                )
                menuViewModel.updateSearchQuery(searchQuery)
            }

            override fun onItemFavoritesClicked(item: Dish) {
                menuViewModel.updateFavoriteDish(item.id, !item.favorite)
            }

            override fun onInsertItemToCartClicked(item: Dish) {
                insertItemToCart(item)
            }
        })
        recyclerViewSearchResult.adapter = searchResultAdapter
        menuViewModel.getSearchResultDishes("")
            .observe(viewLifecycleOwner, { searchResultAdapter.updateData(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.search_menu)
        searchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.search_hint)

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                setSearchVisibility(true)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                setSearchVisibility(false)
                return true
            }
        })

        searchView
            .onQueryTextListenerFlow()
            .debounce(1000)
            .onEach { newText ->
                newText?.let {
                    if (it.isNotBlank()) {
                        searchQuery = it
                        setSearchResultVisibility(true)
                        menuViewModel.getSearchResultCategories(it)
                        menuViewModel.getSearchResultDishes(it)

                        scrollViewMenu.scrollTo(0, scrollViewMenu.top)
                    } else {
                        setSearchResultVisibility(false)
                    }
                }
            }
            .launchIn(lifecycleScope)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setSearchVisibility(isVisible: Boolean) {
        if (isVisible) {
            recyclerViewSearch.visibility = View.VISIBLE
            recyclerViewMenu.visibility = View.GONE
        } else {
            recyclerViewSearch.visibility = View.GONE
            recyclerViewMenu.visibility = View.VISIBLE
        }
    }

    private fun setSearchResultVisibility(isVisible: Boolean) {
        if (isVisible) {
            recyclerViewSearch.visibility = View.GONE
            groupSearchResult.visibility = View.VISIBLE
            recyclerViewMenu.visibility = View.GONE
        } else {
            recyclerViewSearch.visibility = View.VISIBLE
            groupSearchResult.visibility = View.GONE
            recyclerViewMenu.visibility = View.GONE
        }
    }

    private fun insertItemToCart(item: Dish) {
        menuViewModel.insertItemToCart(item.id, 1, item.price)

        notifyShort("Блюдо \"${item.name}\" добавлено в корзину (1 шт.)")
    }

    private fun SearchView.onQueryTextListenerFlow() = callbackFlow {
        val callback = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                trySend(newText)
                return true
            }
        }

        setOnQueryTextListener(callback)

        awaitClose { setOnQueryTextListener(null) }
    }
}
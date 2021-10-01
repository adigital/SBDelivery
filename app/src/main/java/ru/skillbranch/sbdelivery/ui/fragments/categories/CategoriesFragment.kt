package ru.skillbranch.sbdelivery.ui.fragments.categories

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.RootActivity
import ru.skillbranch.sbdelivery.data.local.entities.Category
import ru.skillbranch.sbdelivery.viewmodels.fragments.CategoriesViewModel

class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var mMenuItem: Menu

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        categoriesViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false

        inflater.inflate(R.menu.sort_sort, menu)

        super.onCreateOptionsMenu(menu, inflater)

        mMenuItem = menu

        when (categoriesViewModel.getSortMode().itemNumber) {
            1 -> {
                setMenuIcon(R.id.menuItemSort1, categoriesViewModel.getSortMode().isAscending)
            }
            2 -> {
                setMenuIcon(R.id.menuItemSort2, categoriesViewModel.getSortMode().isAscending)
            }
            3 -> {
                setMenuIcon(R.id.menuItemSort3, categoriesViewModel.getSortMode().isAscending)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemSort1 -> {
                if (categoriesViewModel.getSortMode().itemNumber == 1 && categoriesViewModel.getSortMode().isAscending) {
                    setMenuIcon(item.itemId, false)
                    categoriesViewModel.setSortMode(1, false)
                } else {
                    setMenuIcon(item.itemId, true)
                    categoriesViewModel.setSortMode(1, true)
                }
                mMenuItem.findItem(R.id.menuItemSort2).icon = null
                mMenuItem.findItem(R.id.menuItemSort3).icon = null
                return true
            }
            R.id.menuItemSort2 -> {
                if (categoriesViewModel.getSortMode().itemNumber == 2 && !categoriesViewModel.getSortMode().isAscending) {
                    setMenuIcon(item.itemId, false)
                    categoriesViewModel.setSortMode(2, true)
                } else {
                    setMenuIcon(item.itemId, true)
                    categoriesViewModel.setSortMode(2, false)
                }
                mMenuItem.findItem(R.id.menuItemSort1).icon = null
                mMenuItem.findItem(R.id.menuItemSort3).icon = null
                return true
            }
            R.id.menuItemSort3 -> {
                if (categoriesViewModel.getSortMode().itemNumber == 3 && !categoriesViewModel.getSortMode().isAscending) {
                    setMenuIcon(item.itemId, false)
                    categoriesViewModel.setSortMode(3, true)
                } else {
                    setMenuIcon(item.itemId, true)
                    categoriesViewModel.setSortMode(3, false)
                }
                mMenuItem.findItem(R.id.menuItemSort1).icon = null
                mMenuItem.findItem(R.id.menuItemSort2).icon = null
                return true
            }
        }

        return false
    }

    private fun initView(view: View) {
        val args: CategoriesFragmentArgs by navArgs()

        (activity as RootActivity).supportActionBar?.title = args.name

        categoriesViewModel.getCategories(args.categoryId)
            .observe(viewLifecycleOwner, {
                tabLayout = view.findViewById(R.id.tlCategories)
                viewPager = view.findViewById(R.id.vp2Categories)
                val category: List<Category>

                if (it.isNotEmpty()) {
                    category = it
                    tabLayout.visibility = View.VISIBLE
                } else {
                    category =
                        listOf(Category(args.categoryId, args.name, 0, null, null, true, 0, 0))
                    tabLayout.visibility = View.GONE
                }
                viewPager.adapter = CategoryViewPagerAdapter(this, category)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = category[position].name
                }.attach()
            })
    }

    private fun setMenuIcon(menuItem: Int, isAscending: Boolean) {
        if (isAscending) {
            mMenuItem.findItem(menuItem).setIcon(R.drawable.ic_downward)
        } else {
            mMenuItem.findItem(menuItem).setIcon(R.drawable.ic_upward)
        }
    }
}
package ru.skillbranch.sbdelivery.ui.fragments.orders

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.viewmodels.fragments.OrdersViewModel

class OrdersFragment : Fragment() {

    private lateinit var viewModel: OrdersViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var progressOrders: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
            if (!isAuth) {
                findNavController().navigate(
                    OrdersFragmentDirections.actionNavOrdersToNavAuth("nav_orders")
                )
            } else {
                initView(view)
            }
        })
    }

    private fun initView(view: View) {
        tabLayout = view.findViewById(R.id.tlOrders)
        viewPager = view.findViewById(R.id.vp2Orders)
        progressOrders = view.findViewById(R.id.progressOrders)

        val tabsLabels = mutableListOf<String>()
        for (index in 0 until tabLayout.tabCount) {
            tabsLabels.add(tabLayout.getTabAt(index)?.text.toString())
        }

        viewPager.adapter = OrdersViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsLabels[position]
        }.attach()

        progressOrders.visibility = ProgressBar.VISIBLE
        viewModel.updateOrders().observe(viewLifecycleOwner, {
            if (it) progressOrders.visibility = ProgressBar.GONE
        })
    }
}
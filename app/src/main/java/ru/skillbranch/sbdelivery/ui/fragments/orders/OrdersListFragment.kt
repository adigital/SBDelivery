package ru.skillbranch.sbdelivery.ui.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entities.Order
import ru.skillbranch.sbdelivery.viewmodels.fragments.OrdersViewModel

class OrdersListFragment : Fragment() {

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var ordersAdapter: OrdersAdapter
    private var tabPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        ordersViewModel =
            ViewModelProvider(requireParentFragment()).get(OrdersViewModel::class.java)

        return inflater.inflate(R.layout.fragment_orders_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("tabPosition") }?.apply {
            tabPosition = getInt("tabPosition")
            initView(view)
        }
    }

    private fun initView(view: View) {
        val rvOrderList: RecyclerView = view.findViewById(R.id.rvOrderList)

        ordersAdapter = OrdersAdapter(object : OrdersAdapter.Callback {
            override fun onItemClicked(item: Order) {
                findNavController().navigate(
                    OrdersFragmentDirections.actionNavOrdersToNavOrderItem(item.id)
                )
            }
        })
        rvOrderList.adapter = ordersAdapter

        ordersViewModel.getOrders().observe(viewLifecycleOwner, {
            when (tabPosition) {
                0 -> {
                    ordersAdapter.updateData(it.filter { order -> !order.completed })
                }
                1 -> {
                    ordersAdapter.updateData(it.filter { order -> order.completed })
                }
            }
        })
    }
}
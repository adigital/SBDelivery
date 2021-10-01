package ru.skillbranch.sbdelivery.ui.fragments.orderitem

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.RootActivity
import ru.skillbranch.sbdelivery.databinding.FragmentOrderItemBinding
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.extensions.showDialogNoYes
import ru.skillbranch.sbdelivery.viewmodels.fragments.OrderItemViewModel

class OrderItemFragment : Fragment() {

    private lateinit var viewModel: OrderItemViewModel
    private lateinit var binding: FragmentOrderItemBinding
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(OrderItemViewModel::class.java)

        var orderId: String? = null
        arguments?.takeIf { it.containsKey("orderId") }?.apply {
            orderId = getString("orderId")
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_item, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.orderItemFragment = this

        // If use ViewModel in binding
        binding.viewModel = viewModel

        val adapter = OrderItemAdapter()
        binding.rvItemOrderList.adapter = adapter

        orderId?.let {
            viewModel.getOrderWithItems(it).observe(viewLifecycleOwner, { orderWithItems ->
                orderWithItems?.let {
                    adapter.submitList(orderWithItems.orderItems)
                }
            })
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: OrderItemFragmentArgs by navArgs()

        (activity as RootActivity).supportActionBar?.title = "Заказ № " + args.orderId

        viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
            if (!isAuth) {
                findNavController().navigate(OrderItemFragmentDirections.actionNavOrderItemToNavHome())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onAction() {
        viewModel.order.value?.let { it ->
            if (it.order.completed) {
                viewModel.getCartItemsCount().observe(viewLifecycleOwner, { itemsCount ->
                    if (itemsCount > 0) {
                        showDialogNoYes(getMessage(itemsCount), { updateCart(itemsCount) }, {})
                    } else {
                        updateCart(itemsCount)
                    }
                })
            } else {
                progressShow.postValue(true)

                viewModel.cancelOrder(it.order.id).observe(viewLifecycleOwner, { isOk ->
                    progressShow.postValue(false)
                    if (isOk) {
                        notifyShort("Заказ успешно отменен")
                    }
                })
            }
        }
    }

    private fun getMessage(itemsCount: Int): String {
        val dishString = if (itemsCount % 100 in 5..20) "$itemsCount блюд" else {
            when (itemsCount % 10) {
                1 -> "$itemsCount блюдо"
                in 2..4 -> "$itemsCount блюда"
                else -> "$itemsCount блюд"
            }
        }

        return "Ваша корзина уже содержит $dishString. Очистить корзину?"
    }

    private fun updateCart(itemsCount: Int) {
        viewModel.updateCart(itemsCount)

        findNavController().navigate(
            OrderItemFragmentDirections.actionNavOrderItemToNavCart()
        )
    }
}
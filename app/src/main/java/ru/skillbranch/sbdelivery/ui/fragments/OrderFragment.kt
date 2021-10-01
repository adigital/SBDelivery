package ru.skillbranch.sbdelivery.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.remote.req.OrderReq
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.data.remote.res.toAddressString
import ru.skillbranch.sbdelivery.viewmodels.fragments.OrderViewModel

class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var progressOrder: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_order, container, false)
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
                    OrderFragmentDirections.actionNavOrderToNavAuth("nav_order")
                )
            } else {
                initView(view)
            }
        })
    }

    private fun initView(view: View) {
        val args: OrderFragmentArgs by navArgs()

        val tvOrderAddressFull: TextView = view.findViewById(R.id.tvOrderAddressFull)
        val btnOrderEdit: AppCompatButton = view.findViewById(R.id.btnOrderEdit)
        val btnOrderMap: AppCompatButton = view.findViewById(R.id.btnOrderMap)

        val etOrderAddress1: EditText = view.findViewById(R.id.etOrderAddress1)
        val etOrderAddress2: EditText = view.findViewById(R.id.etOrderAddress2)
        val etOrderAddress3: EditText = view.findViewById(R.id.etOrderAddress3)
        val etOrderAddress4: EditText = view.findViewById(R.id.etOrderAddress4)
        val etOrderComment: EditText = view.findViewById(R.id.etOrderComment)

        val btnOrderCheckout: AppCompatButton = view.findViewById(R.id.btnOrderCheckout)
        progressOrder = view.findViewById(R.id.progressOrder)

        if (args.city != null && args.street != null && args.house != null) {
            val addressRes = AddressRes(args.city, args.street, args.house)
            tvOrderAddressFull.text = addressRes.toAddressString()
            btnOrderEdit.text = "Редактировать"

            btnOrderCheckout.isEnabled = true
        }

        btnOrderEdit.setOnClickListener {
            findNavController().navigate(
                OrderFragmentDirections.actionOrderFragmentToAddressFragment()
            )
        }

        btnOrderMap.setOnClickListener {
            findNavController().navigate(
                OrderFragmentDirections.actionOrderFragmentToMapsFragment()
            )
        }

        btnOrderCheckout.setOnClickListener {
            progressOrder.visibility = ProgressBar.VISIBLE

            viewModel.createOrder(
                OrderReq(
                    tvOrderAddressFull.text.toString(),
                    if (etOrderAddress1.text.isNotBlank()) etOrderAddress1.text.toString()
                        .toInt() else 0,
                    if (etOrderAddress2.text.isNotBlank()) etOrderAddress2.text.toString()
                        .toInt() else 0,
                    if (etOrderAddress3.text.isNotBlank()) etOrderAddress3.text.toString() else "",
                    if (etOrderAddress4.text.isNotBlank()) etOrderAddress4.text.toString() else "",
                    if (etOrderComment.text.isNotBlank()) etOrderComment.text.toString() else ""
                )
            ).observe(viewLifecycleOwner, { order ->
                progressOrder.visibility = ProgressBar.GONE

                order?.also { newOrder ->
                    findNavController().navigate(
                        OrderFragmentDirections.actionNavOrderToNavOrderItem(newOrder.id)
                    )
                }
            })
        }
    }
}
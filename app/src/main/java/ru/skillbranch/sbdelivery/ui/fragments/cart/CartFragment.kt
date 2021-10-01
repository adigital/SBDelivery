package ru.skillbranch.sbdelivery.ui.fragments.cart

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.models.CartItemModel
import ru.skillbranch.sbdelivery.extensions.showDialogOk
import ru.skillbranch.sbdelivery.viewmodels.fragments.CartViewModel


class CartFragment : Fragment() {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var progressCart: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_cart, container, false)
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
        val tvCartEmptyCart: TextView = view.findViewById(R.id.tvCartEmptyCart)
        val nsvCart: NestedScrollView = view.findViewById(R.id.nsvCart)
        val rvCartList: RecyclerView = view.findViewById(R.id.rvCartList)
        val etCartPromo: EditText = view.findViewById(R.id.etCartPromo)
        val btnCartPromo: AppCompatButton = view.findViewById(R.id.btnCartPromo)
        val clCart: ConstraintLayout = view.findViewById(R.id.clCart)
        val tvCartPrice: TextView = view.findViewById(R.id.tvCartPrice)
        val btnCartCheckout: AppCompatButton = view.findViewById(R.id.btnCartCheckout)
        progressCart = view.findViewById(R.id.progressCart)

        val decor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rvCartList.addItemDecoration(decor)

        viewModel.getCart().observe(viewLifecycleOwner, {
            tvCartPrice.text = (it?.total ?: 0).toString().plus(" ₽")
        })

        viewModel.getCartItems().observe(viewLifecycleOwner, {
            cartAdapter.updateData(it)

            if (it.isEmpty()) {
                tvCartEmptyCart.visibility = View.VISIBLE
                nsvCart.visibility = View.GONE
                clCart.visibility = View.GONE
            } else {
                tvCartEmptyCart.visibility = View.GONE
                nsvCart.visibility = View.VISIBLE
                clCart.visibility = View.VISIBLE
            }
        })

        cartAdapter = CartAdapter(object : CartAdapter.Callback {
            override fun onCounterClicked(item: CartItemModel) {
                viewModel.updateCart(item)
            }

            override fun onDishClicked(item: CartItemModel) {
                findNavController().navigate(
                    CartFragmentDirections.actionNavCartToNavDish(item.id, item.name)
                )
            }
        })
        rvCartList.adapter = cartAdapter

        etCartPromo.addTextChangedListener {
            if (it!!.isEmpty()) {
                btnCartPromo.isClickable = false
                btnCartPromo.text = "Применить"
                btnCartPromo.setTextColor(
                    ContextCompat.getColor(App.context, R.color.tab_text_color)
                )
            } else {
                btnCartPromo.isClickable = true
                btnCartPromo.text = "Отменить"
                btnCartPromo.setTextColor(ContextCompat.getColor(App.context, R.color.white))
            }
        }

        btnCartCheckout.setOnClickListener {
            progressCart.visibility = ProgressBar.VISIBLE

            viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
                if (isAuth) {
                    viewModel.updateCartNet().observe(
                        viewLifecycleOwner, { result ->
                            progressCart.visibility = ProgressBar.GONE
                            when (result) {
                                1 -> {
                                    findNavController().navigate(
                                        CartFragmentDirections.actionNavCartToOrderFragment(
                                            null,
                                            null,
                                            null
                                        )
                                    )
                                }
                                2 -> {
                                    showDialogOk("Корзина была изменена")
                                }
                            }
                        })
                } else {
                    findNavController().navigate(
                        CartFragmentDirections.actionNavCartToNavAuth("nav_cart")
                    )
                }
            })
        }
    }
}
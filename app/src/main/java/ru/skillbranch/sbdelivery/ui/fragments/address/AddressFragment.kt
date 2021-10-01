package ru.skillbranch.sbdelivery.ui.fragments.address

import android.os.Bundle
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.remote.res.AddressRes
import ru.skillbranch.sbdelivery.data.remote.res.isFull
import ru.skillbranch.sbdelivery.data.remote.res.toAddressString
import ru.skillbranch.sbdelivery.viewmodels.fragments.AddressViewModel

class AddressFragment : Fragment() {

    private lateinit var viewModel: AddressViewModel
    private lateinit var addressAdapter: AddressAdapter
    private var latestSearch: String = ""
    lateinit var addressRes: AddressRes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)

        viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
            if (!isAuth) {
                findNavController().navigate(AddressFragmentDirections.actionNavAddressToNavHome())
            }
        })
    }

    private fun initView(view: View) {
        val etAddress: EditText = view.findViewById(R.id.etAddress)
        val rvAddress: RecyclerView = view.findViewById(R.id.rvAddress)
        val btnAddress: AppCompatButton = view.findViewById(R.id.btnAddress)

        viewModel.address.observe(viewLifecycleOwner, {
            addressAdapter.updateData(it)
        })

        addressAdapter = AddressAdapter(object : AddressAdapter.Callback {
            override fun onAddressClicked(item: AddressRes) {
                item.toAddressString()?.also {
                    latestSearch = it
                    etAddress.setText(it)

                    btnAddress.isEnabled = item.isFull()

                    if (btnAddress.isEnabled) {
                        addressRes = item
                    }
                }
            }
        })
        rvAddress.adapter = addressAdapter

        etAddress
            .textInputAsFlow()
            .debounce(500)
            .onEach {
                if (latestSearch != it.toString()) {
                    viewModel.checkInputNet(it.toString())

                    btnAddress.isEnabled = false
                }
                latestSearch = it.toString()
            }
            .launchIn(lifecycleScope)

        btnAddress.setOnClickListener {
            findNavController().navigate(
                AddressFragmentDirections.actionAddressFragmentToOrderFragment(
                    addressRes.city,
                    addressRes.street,
                    addressRes.house
                )
            )
        }
    }
}

fun EditText.textInputAsFlow() = callbackFlow {
    val watcher: TextWatcher = doOnTextChanged { textInput: CharSequence?, _, _, _ ->
        this.trySend(textInput).isSuccess
    }

    awaitClose { this@textInputAsFlow.removeTextChangedListener(watcher) }
}
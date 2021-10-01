package ru.skillbranch.sbdelivery.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentRegBinding
import ru.skillbranch.sbdelivery.viewmodels.fragments.RegViewModel

class RegFragment : Fragment() {

    private lateinit var viewModel: RegViewModel
    private lateinit var binding: FragmentRegBinding
    private val args: AuthFragmentArgs by navArgs()
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(RegViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.regFragment = this

        // If use ViewModel in binding
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onRegistration() {
        if (viewModel.isValidation()) {
            progressShow.postValue(true)

            viewModel.registration().observe(viewLifecycleOwner, { isOk ->
                progressShow.postValue(false)

                if (isOk) {
                    when (args.source) {
                        "nav_orders" -> findNavController().navigate(RegFragmentDirections.actionNavRegToNavOrders())
                        "nav_cart" -> findNavController().navigate(RegFragmentDirections.actionNavRegToNavCart())
                        "nav_order" -> findNavController().navigate(RegFragmentDirections.actionNavRegToNavHome())
                        "nav_profile" -> findNavController().navigate(RegFragmentDirections.actionNavRegToNavProfile())
                        "nav_dish" -> findNavController().popBackStack(R.id.nav_dish, false)
                    }
                }
            })
        }
    }

    fun onLogin() {
        findNavController().popBackStack()
    }
}
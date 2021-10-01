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
import ru.skillbranch.sbdelivery.databinding.FragmentAuthBinding
import ru.skillbranch.sbdelivery.extensions.showDialogNoYes
import ru.skillbranch.sbdelivery.viewmodels.SyncMode
import ru.skillbranch.sbdelivery.viewmodels.fragments.AuthViewModel

class AuthFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentAuthBinding
    private val args: AuthFragmentArgs by navArgs()
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.authFragment = this

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

    fun onLogin() {
        progressShow.postValue(true)

        viewModel.login().observe(viewLifecycleOwner, { isOk ->
            progressShow.postValue(false)

            if (isOk) {
                viewModel.getFavoriteDishesCount().observe(viewLifecycleOwner, { count ->
                    if (count > 0) {
                        showDialogNoYes(
                            "Синхронизировать список избранных блюд с сервером?",
                            { viewModel.syncFavoriteDishes(SyncMode.NET_TO_DAO_TO_NET) },
                            { viewModel.syncFavoriteDishes(SyncMode.NET_TO_DAO_CLEAR) })

                    } else {
                        viewModel.syncFavoriteDishes(SyncMode.NET_TO_DAO)
                    }

                    when (args.source) {
                        "nav_orders" -> findNavController().navigate(
                            AuthFragmentDirections.actionNavAuthToNavOrders()
                        )
                        "nav_cart" -> findNavController().popBackStack()
                        "nav_order" -> findNavController().navigate(
                            AuthFragmentDirections.actionNavAuthToNavHome()
                        )
                        "nav_profile" -> findNavController().navigate(
                            AuthFragmentDirections.actionNavAuthToNavProfile()
                        )
                        "nav_dish" -> findNavController().popBackStack()
                    }
                })
            }
        })
    }

    fun onRegistration() {
        findNavController().navigate(AuthFragmentDirections.actionNavAuthToNavReg(args.source))
    }

    fun onRecovery() {
        findNavController().navigate(AuthFragmentDirections.actionNavAuthToRecoveryFragment())
    }
}
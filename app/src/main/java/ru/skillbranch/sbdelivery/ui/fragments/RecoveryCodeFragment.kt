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
import ru.skillbranch.sbdelivery.databinding.FragmentRecoveryCodeBinding
import ru.skillbranch.sbdelivery.extensions.hideKeyboard
import ru.skillbranch.sbdelivery.viewmodels.fragments.RecoveryCodeViewModel

class RecoveryCodeFragment : Fragment() {

    private lateinit var viewModel: RecoveryCodeViewModel
    private lateinit var binding: FragmentRecoveryCodeBinding
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(RecoveryCodeViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recovery_code, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.recoveryCodeFragment = this

        // If use ViewModel in binding
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val args: RecoveryCodeFragmentArgs by navArgs()

        viewModel.email = args.email

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onSend() {
        hideKeyboard()
        progressShow.postValue(true)

        viewModel.recovery().observe(viewLifecycleOwner, { isOk ->
            progressShow.postValue(false)

            if (isOk) {
                findNavController().navigate(
                    RecoveryCodeFragmentDirections.actionNavRecoveryCodeToRecoveryPasswordFragment(
                        viewModel.email,
                        viewModel.code1 + viewModel.code2 + viewModel.code3 + viewModel.code4
                    )
                )
            }
        })
    }
}
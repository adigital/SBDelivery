package ru.skillbranch.sbdelivery.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentRecoveryEmailBinding
import ru.skillbranch.sbdelivery.viewmodels.fragments.RecoveryEmailViewModel

class RecoveryEmailFragment : Fragment() {

    private lateinit var viewModel: RecoveryEmailViewModel
    private lateinit var binding: FragmentRecoveryEmailBinding
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(RecoveryEmailViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recovery_email, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.recoveryEmailFragment = this

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

    fun onSend() {
        progressShow.postValue(true)

        viewModel.recovery().observe(viewLifecycleOwner, { isOk ->
            progressShow.postValue(false)

            if (isOk) {
                findNavController().navigate(
                    RecoveryEmailFragmentDirections.actionNavRecoveryEmailToNavRecoveryCode(
                        viewModel.email
                    )
                )
            }
        })
    }
}
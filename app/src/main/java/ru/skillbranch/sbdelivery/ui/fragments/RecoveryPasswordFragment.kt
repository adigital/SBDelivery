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
import ru.skillbranch.sbdelivery.databinding.FragmentRecoveryPasswordBinding
import ru.skillbranch.sbdelivery.viewmodels.fragments.RecoveryPasswordViewModel

class RecoveryPasswordFragment : Fragment() {

    private lateinit var viewModel: RecoveryPasswordViewModel
    private lateinit var binding: FragmentRecoveryPasswordBinding
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(RecoveryPasswordViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_recovery_password, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.recoveryPasswordFragment = this

        // If use ViewModel in binding
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val args: RecoveryPasswordFragmentArgs by navArgs()

        viewModel.email = args.email
        viewModel.code = args.code

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
                findNavController().popBackStack()
            }
        })
    }
}
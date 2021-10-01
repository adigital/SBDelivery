package ru.skillbranch.sbdelivery.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentProfileBinding
import ru.skillbranch.sbdelivery.ui.dialogs.ChangePasswordDialog
import ru.skillbranch.sbdelivery.viewmodels.fragments.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.profileFragment = this

        // If use ViewModel in binding
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.authState.observe(viewLifecycleOwner, { isAuth ->
            if (!isAuth) {
                findNavController().navigate(
                    ProfileFragmentDirections.actionNavProfileToNavAuth("nav_profile")
                )
            } else {
                initView()
            }
        })
    }

    private fun initView() {
        progressShow.postValue(true)

        viewModel.getProfile().observe(viewLifecycleOwner, { profileRes ->
            progressShow.postValue(false)
            viewModel.firstName.postValue(profileRes.firstName)
            viewModel.lastName.postValue(profileRes.lastName)
            viewModel.email.postValue(profileRes.email)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuItem = menu.findItem(R.id.action_menu)
        menuItem.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onEdit() {
        if (viewModel.isEdit.value!!) {
            if (viewModel.isDataCorrect.value!!) {
                progressShow.postValue(true)

                viewModel.editProfile().observe(viewLifecycleOwner, { profileRes ->
                    progressShow.postValue(false)
                    viewModel.firstName.postValue(profileRes.firstName)
                    viewModel.lastName.postValue(profileRes.lastName)
                    viewModel.email.postValue(profileRes.email)
                })
            }
            viewModel.isEdit.postValue(false)
        } else {
            viewModel.isEdit.postValue(true)
        }
    }

    fun onButton() {
        if (viewModel.isEdit.value!!) {
            viewModel.isEdit.value = false
            viewModel.cancel()
        } else {
            ChangePasswordDialog().show(parentFragmentManager, null)
        }
    }
}
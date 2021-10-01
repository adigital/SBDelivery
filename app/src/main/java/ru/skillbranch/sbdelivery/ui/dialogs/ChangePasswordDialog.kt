package ru.skillbranch.sbdelivery.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.DialogChangePasswordBinding
import ru.skillbranch.sbdelivery.extensions.dpToIntPx
import ru.skillbranch.sbdelivery.viewmodels.ChangePasswordDialogViewModel

class ChangePasswordDialog : DialogFragment() {

    private lateinit var viewModel: ChangePasswordDialogViewModel
    private lateinit var binding: DialogChangePasswordBinding
    var progressShow = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this).get(ChangePasswordDialogViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_change_password, container, false)

        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // If use Fragment in binding
        binding.changePasswordDialog = this

        // If use ViewModel in binding
        binding.viewModel = viewModel

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_corner)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window?.setLayout(App.context.dpToIntPx(300), App.context.dpToIntPx(297))
    }

    fun onSave() {
        progressShow.postValue(true)

        viewModel.changePassword().observe(viewLifecycleOwner, { isOk ->
            progressShow.postValue(false)

            if (isOk) {
                dismiss()
            }
        })
    }

    fun onClose() {
        dismiss()
    }
}
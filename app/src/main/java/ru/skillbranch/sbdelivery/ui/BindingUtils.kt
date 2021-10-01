package ru.skillbranch.sbdelivery.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.extensions.millisToDate
import ru.skillbranch.sbdelivery.ui.fragments.RecoveryCodeFragment
import ru.skillbranch.sbdelivery.viewmodels.ChangePasswordDialogViewModel
import ru.skillbranch.sbdelivery.viewmodels.fragments.ProfileViewModel
import ru.skillbranch.sbdelivery.viewmodels.fragments.RecoveryEmailViewModel
import ru.skillbranch.sbdelivery.viewmodels.fragments.RecoveryPasswordViewModel

@BindingAdapter("dateMillis")
fun TextView.dateMillis(millis: Long) {
    this.text = millisToDate(millis)
}

@BindingAdapter("textColorId")
fun TextView.setTextColorId(colorId: Int) {
    this.setTextColor(colorId)
}

@BindingAdapter("dividerDirection")
fun RecyclerView.dividerDirection(orientation: Int) {
    val decor = DividerItemDecoration(context, orientation)
    this.addItemDecoration(decor)
}

@BindingAdapter("bind:imageUrl")
fun RoundedImageView.imageURL(image: String) {
    Glide.with(this.context)
        .load(image)
        .placeholder(R.drawable.ic_baseline_more_horiz_24)
        .error(R.drawable.ic_baseline_error_24)
        .into(this)
}

@BindingAdapter("app:addTextChangeListener")
fun EditText.addTextChangeListener(viewModel: ViewModel) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when (viewModel) {
                is RecoveryEmailViewModel -> viewModel.checkEmail()
                is RecoveryPasswordViewModel -> viewModel.checkPasswords()
                is ProfileViewModel -> viewModel.checkData()
                is ChangePasswordDialogViewModel -> viewModel.checkPasswords()
            }
        }
    })
}

@BindingAdapter("app:fragment", "app:nextEditText")
fun EditText.addCodeChangeListener(fragment: Fragment, nextEditText: EditText?) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (fragment is RecoveryCodeFragment) {
                p0?.isNotBlank()?.let { isNotBlank ->
                    nextEditText?.isEnabled = isNotBlank
                    if (isNotBlank) {
                        nextEditText?.requestFocus() ?: fragment.onSend()
                    } else {
                        nextEditText?.text?.clear()
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}
package ru.skillbranch.sbdelivery.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.skillbranch.sbdelivery.R

fun Fragment.showDialogOk(text: String) {
    MaterialAlertDialogBuilder(
        requireContext(),
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )
        .setMessage(text)
        .setPositiveButton(getString(android.R.string.ok)) { dialog, _ ->
            dialog.cancel()
        }
        .show()
}

fun Fragment.showDialogNoYes(
    text: String,
    positive: () -> Unit,
    negative: () -> Unit
) {
    showDialogNoYes(requireContext(), text, positive, negative)
}

fun showDialogNoYes(context: Context, text: String, positive: () -> Unit, negative: () -> Unit) {
    MaterialAlertDialogBuilder(
        context,
        R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
    )
        .setMessage(text)
        .setPositiveButton("Да") { _, _ ->
            positive()
        }
        .setNegativeButton("Нет") { _, _ ->
            negative()
        }
        .show()
}
package ru.skillbranch.sbdelivery.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.extensions.dpToIntPx
import ru.skillbranch.sbdelivery.extensions.notifyShort
import ru.skillbranch.sbdelivery.viewmodels.ReviewDialogViewModel

class ReviewDialog : DialogFragment() {

    private lateinit var viewModel: ReviewDialogViewModel
    private lateinit var ivClose: ImageView
    private lateinit var rbRating: RatingBar
    private lateinit var tvText: EditText
    private lateinit var progress: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this).get(ReviewDialogViewModel::class.java)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_corner)
        return inflater.inflate(R.layout.dialog_review, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window?.setLayout(App.context.dpToIntPx(300), App.context.dpToIntPx(315))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    private fun initView(view: View) {
        ivClose = view.findViewById(R.id.ivDialogReviewClose)
        rbRating = view.findViewById(R.id.rbDialogReviewRating)
        tvText = view.findViewById(R.id.tvDialogReviewText)
        val btnSend: AppCompatButton = view.findViewById(R.id.btnDialogReviewSend)
        progress = view.findViewById(R.id.progressDialogReview)

        ivClose.setOnClickListener {
            dismiss()
        }

        btnSend.setOnClickListener {
            val bundle = Bundle()

            this.tag?.let { it1 ->
                progress.visibility = ProgressBar.VISIBLE

                viewModel.addReviewNet(it1, rbRating.rating, tvText.text.toString())
                    .observe(viewLifecycleOwner, { result ->
                        when (result) {
                            1 -> {
                                bundle.putBoolean("isOk", true)
                                setFragmentResult("requestKey", bundle)

                                progress.visibility = ProgressBar.GONE
                                dismiss()
                                notifyShort("Отзыв будет опубликован после проверки модератором")
                            }
                            2 -> {
                                progress.visibility = ProgressBar.GONE
                                notifyShort("Ошибка при отправке. Попробуйте позже")
                            }
                        }
                    })
            }
        }

        rbRating.setOnRatingBarChangeListener { ratingBar, _, _ ->
            btnSend.isEnabled = ratingBar.rating > 0
        }
    }
}
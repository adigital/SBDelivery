package ru.skillbranch.sbdelivery.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import ru.skillbranch.sbdelivery.R

class CounterView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var onClickListener: OnClickListener? = null

    var textSize: Int = 24
        set(value) {
            field = value
            countTextView.textSize = value.toFloat()
        }

    var isZeroAllowed = false
        set(value) {
            field = value
            updateState()
        }

    private val minusButton: AppCompatButton
    private val countTextView: AppCompatTextView
    private val plusButton: AppCompatButton

    private var counter = 1

    init {
        LayoutInflater.from(context).inflate(R.layout.view_counter, this, true)
        orientation = HORIZONTAL

        minusButton = findViewById(R.id.minusButton)
        countTextView = findViewById(R.id.countTextView)
        plusButton = findViewById(R.id.plusButton)

        minusButton.alpha = 0.6f
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        minusButton.setOnClickListener {
            counter = counter.dec()
            if ((counter == 0 && !isZeroAllowed) || (counter == -1 && isZeroAllowed)) {
                counter.inc()
            } else {
                updateState()
            }

            onClickListener?.onClick(it)
        }

        plusButton.setOnClickListener {
            counter = counter.inc()
            updateState()

            onClickListener?.onClick(it)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)

        onClickListener = l
    }

    private fun updateState() {
        countTextView.text = counter.toString()

        if ((counter == 1 && !isZeroAllowed) || (counter == 0 && isZeroAllowed)) {
            minusButton.isEnabled = false
            minusButton.alpha = 0.6f
        } else {
            minusButton.isEnabled = true
            minusButton.alpha = 1f
        }
    }

    fun getCounter() = counter

    fun setCounter(count: Int) {
        counter = count
        updateState()
    }
}
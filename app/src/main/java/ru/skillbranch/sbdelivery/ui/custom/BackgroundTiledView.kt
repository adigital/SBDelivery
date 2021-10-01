package ru.skillbranch.sbdelivery.ui.custom

import android.content.Context
import android.graphics.Shader
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import ru.skillbranch.sbdelivery.R

class BackgroundTiledView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    init {
        val d = ContextCompat.getDrawable(context, R.drawable.ic_background_pattern)
        this.setImageDrawable(TileDrawable(d!!, Shader.TileMode.REPEAT))
    }
}
package ru.skillbranch.sbdelivery.ui.custom

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import ru.skillbranch.sbdelivery.App
import ru.skillbranch.sbdelivery.extensions.dpToIntPx

class SpaceItemDecoration(private val rightDp: Int, private val bottomDp: Int) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.right = App.context.dpToIntPx(rightDp)
        outRect.bottom = App.context.dpToIntPx(bottomDp)
    }
}
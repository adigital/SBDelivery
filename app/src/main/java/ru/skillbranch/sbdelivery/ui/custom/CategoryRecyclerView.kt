package ru.skillbranch.sbdelivery.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import ru.skillbranch.sbdelivery.extensions.dpToIntPx

class CategoryRecyclerView(context: Context, attributeSet: AttributeSet) :
    RecyclerView(context, attributeSet) {
    init {
        layoutManager = GridAutoFitLayoutManager(context, context.dpToIntPx(158))
    }
}

class GridAutoFitLayoutManager(context: Context, columnWidth: Int) : GridLayoutManager(context, 1) {
    private var columnWidth = 0
    private var isColumnWidthChanged = true
    private var lastWidth = 0
    private var lastHeight = 0

    init {
        setColumnWidth(columnWidth)
    }

    private fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        val width = width
        val height = height

        if (columnWidth > 0 && width > 0 && height > 0 && (isColumnWidthChanged || lastWidth != width || lastHeight != height)) {

            val totalSpace: Int = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }

            val spanCount = 1.coerceAtLeast(totalSpace / columnWidth)
            setSpanCount(spanCount)
            isColumnWidthChanged = false
        }

        lastWidth = width
        lastHeight = height
        super.onLayoutChildren(recycler, state)
    }
}
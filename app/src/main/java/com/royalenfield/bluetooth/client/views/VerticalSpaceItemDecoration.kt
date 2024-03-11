package com.royalenfield.bluetooth.client.views

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/***
 * Made by Lokesh Desai (Android4Dev)
 */
class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}
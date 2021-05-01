package com.example.flowwebsocket.extensions

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.flowwebsocket.R

fun LinearLayout.setRowCells(context: Context, rowPos: Int, onClick: (View, Int, Int) -> Unit) {
    for (columnPos in 0..3) {
        val newImageView = Button(context)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            0.25f
        )
        newImageView.layoutParams = params
        newImageView.background = ContextCompat.getDrawable(context, R.drawable.imageview_border_background)
        newImageView.setOnClickListener {
            onClick.invoke(it, rowPos, columnPos)
        }
        this.addView(newImageView)
    }
}
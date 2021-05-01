package com.example.flowwebsocket.extensions

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.flowwebsocket.R

fun LinearLayout.setRowCells(context: Context, onClick: () -> Unit) {
    for (i in 0..3) {
        val newImageView = Button(context)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            0.25f
        )
        newImageView.layoutParams = params
        newImageView.background = ContextCompat.getDrawable(context, R.drawable.imageview_border_background)
        newImageView.setOnClickListener {
            onClick.invoke()
        }
        this.addView(newImageView)
    }
}
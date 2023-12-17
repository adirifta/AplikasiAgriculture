package com.example.aplikasiagriculture

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContent)

    // This method will be called every time the MarkerView is redrawn
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvContent.text = "Value: ${e?.y}"
        super.refreshContent(e, highlight)
    }

    // This method enables using the custom layout size
    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}
package com.example.aplikasiagriculture

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class AnalisaGrafik : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analisa_grafik)

        // Ambil referensi ke LineChart dari layout
        val lineChart: LineChart = findViewById(R.id.lineChart)

        // Contoh data untuk grafik
        val entries = mutableListOf<Entry>()
        for (i in 0..10) {
            // Tambahkan data dengan nilai acak untuk contoh
            entries.add(Entry(i.toFloat(), Random().nextFloat() * 100))
        }

        // Set data untuk LineChart
        val dataSet = LineDataSet(entries, "Label Grafik")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Konfigurasi sumbu X
        val xAxis: XAxis = lineChart.xAxis
        xAxis.valueFormatter = MyXAxisValueFormatter() // Pengaturan formatter untuk sumbu X
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Refresh grafik
        lineChart.invalidate()
    }

    // Formatter untuk sumbu X
    inner class MyXAxisValueFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            // Mengembalikan label sesuai dengan nilai sumbu X
            return value.toInt().toString()
        }
    }
}
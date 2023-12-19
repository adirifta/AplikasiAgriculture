package com.example.aplikasiagriculture

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AnalisaGrafik : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private val lineDataSetList = mutableListOf<LineDataSet>()
    private lateinit var textViewIntensitasCahaya: TextView
    private lateinit var textViewKelembapanRuangan: TextView
    private lateinit var textViewKelembapanTanah: TextView
    private lateinit var textViewSuhuRuangan: TextView
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analisa_grafik)

        // Inisialisasi Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // Inisialisasi TextView
        textViewIntensitasCahaya = findViewById(R.id.textViewIntensitasCahaya)
        textViewKelembapanRuangan = findViewById(R.id.textViewKelembapanRuangan)
        textViewKelembapanTanah = findViewById(R.id.textViewKelembapanTanah)
        textViewSuhuRuangan = findViewById(R.id.textViewSuhuRuangan)

        // Inisialisasi LineChart
        lineChart = findViewById(R.id.lineChart)
        setupLineChart()

        // Simpan data ke Firebase Realtime Database
        saveDataToFirebase()
        // Mendengarkan perubahan data di Firebase
        listenToFirebaseData()
        setupLineDataSet()
    }

    private fun saveDataToFirebase() {
        // Misalkan Anda ingin menyimpan data di "tubes_data" di Firebase
        val dataRef = databaseReference.child("Tubes")

        // Dummy data for the chart
        val data = mapOf(
            "IntensitasCahaya" to 100,
            "KelembapanRuangan" to 60,
            "KelembapanTanah" to 40,
            "SuhuRuangan" to 25
        )

        // Simpan data ke Firebase
        dataRef.setValue(data)
    }

    private fun listenToFirebaseData() {
        val dataRef = databaseReference.child("Tubes")

        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val intensitasCahaya = snapshot.child("IntensitasCahaya").value.toString()
                    val kelembapanRuangan = snapshot.child("KelembapanRuangan").value.toString()
                    val kelembapanTanah = snapshot.child("KelembapanTanah").value.toString()
                    val suhuRuangan = snapshot.child("SuhuRuangan").value.toString()

                    // Update TextView
                    textViewIntensitasCahaya.text = "Intensitas Cahaya: $intensitasCahaya"
                    textViewKelembapanRuangan.text = "Kelembapan Ruangan: $kelembapanRuangan"
                    textViewKelembapanTanah.text = "Kelembapan Tanah: $kelembapanTanah"
                    textViewSuhuRuangan.text = "Suhu Ruangan: $suhuRuangan"

                    // Update LineChart
                    updateLineChart(
                        intensitasCahaya.toFloat(),
                        kelembapanRuangan.toFloat(),
                        kelembapanTanah.toFloat(),
                        suhuRuangan.toFloat()
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setupLineChart() {
        lineChart.description = Description().apply { text = "" }
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis: YAxis = lineChart.axisLeft
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawZeroLine(true)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.isEnabled = false
    }

    private fun updateLineChart(
        intensitasCahaya: Float,
        kelembapanRuangan: Float,
        kelembapanTanah: Float,
        suhuRuangan: Float
    ) {
        // Add new data entry
        val entries = mutableListOf<Entry>()
        entries.add(Entry(lineDataSetList[0].entryCount.toFloat(), intensitasCahaya))
        entries.add(Entry(lineDataSetList[1].entryCount.toFloat(), kelembapanRuangan))
        entries.add(Entry(lineDataSetList[2].entryCount.toFloat(), kelembapanTanah))
        entries.add(Entry(lineDataSetList[3].entryCount.toFloat(), suhuRuangan))

        for ((index, entry) in entries.withIndex()) {
            lineDataSetList[index].addEntry(entry)
        }

        val lineData = LineData(lineDataSetList as List<ILineDataSet>?)
        lineChart.data = lineData
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    }

    private fun setupLineDataSet() {
        val colors = mutableListOf<Int>(
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.YELLOW
        )

        for (i in 0 until 4) {
            val lineDataSet = LineDataSet(null, "Label $i")
            lineDataSet.color = colors[i]
            lineDataSet.valueTextColor = Color.BLACK
            lineDataSet.setCircleColor(colors[i])
            lineDataSet.lineWidth = 2f
            lineDataSet.circleRadius = 4f
            lineDataSet.setDrawCircleHole(false)
            lineDataSet.setDrawValues(false)

            lineDataSetList.add(lineDataSet)
        }
    }
}
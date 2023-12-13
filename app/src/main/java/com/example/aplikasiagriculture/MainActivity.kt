package com.example.aplikasiagriculture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var graphViewIntensitasCahaya: GraphView
    private lateinit var graphViewKelembapanRuangan: GraphView
    private lateinit var graphViewKelembapanTanah: GraphView
    private lateinit var graphViewSuhuRuangan: GraphView
    private lateinit var series: LineGraphSeries<DataPoint>
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    private lateinit var intensitasCahayaRef: DatabaseReference
    private lateinit var kelembapanRuanganRef: DatabaseReference
    private lateinit var kelembapanTanahRef: DatabaseReference
    private lateinit var suhuRuanganRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphViewIntensitasCahaya = findViewById(R.id.graphViewIntensitasCahaya)
        graphViewKelembapanRuangan = findViewById(R.id.graphViewKelembapanRuangan)
        graphViewKelembapanTanah = findViewById(R.id.graphViewKelembapanTanah)
        graphViewSuhuRuangan = findViewById(R.id.graphViewSuhuRuangan)

        series = LineGraphSeries()
        graphViewIntensitasCahaya.addSeries(series)  // Use the correct graphView reference

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Tubes")
        intensitasCahayaRef = database.getReference("Tubes/IntensitasCahaya")
        kelembapanRuanganRef = database.getReference("Tubes/KelembapanRuangan")
        kelembapanTanahRef = database.getReference("Tubes/KelembapanTanah")
        suhuRuanganRef = database.getReference("Tubes/SuhuRuangan")
        setupGraphView()

        // You can remove setListeners() since the button and text input are removed
    }

    private fun setupGraphView() {
        graphViewIntensitasCahaya.gridLabelRenderer.numHorizontalLabels = 3

        graphViewIntensitasCahaya.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    sdf.format(Date(value.toLong()))
                } else {
                    super.formatLabel(value, isValueX)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val hourlyData = mutableMapOf<Int, MutableList<Double>>()

                for (myDataSnapshot in dataSnapshot.children) {
                    val timestamp = myDataSnapshot.child("xValue").getValue(Long::class.java)
                    val value = myDataSnapshot.child("yValue").getValue(Double::class.java)

                    if (timestamp != null && value != null) {
                        // Extract the hour from the timestamp
                        val hour = Calendar.getInstance().apply {
                            timeInMillis = timestamp
                        }.get(Calendar.HOUR_OF_DAY)

                        // Add the value to the corresponding hour
                        if (hourlyData.containsKey(hour)) {
                            hourlyData[hour]?.add(value)
                        } else {
                            hourlyData[hour] = mutableListOf(value)
                        }
                    }
                }

                val averageHourlyData = hourlyData.map { (hour, values) ->
                    val average = values.average()
                    DataPoint(hour.toDouble(), average)
                }.toTypedArray()

                series.resetData(averageHourlyData)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        // Add ValueEventListener for IntensitasCahaya
        intensitasCahayaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle data change for IntensitasCahaya
                // Update your UI or process data as needed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        // Add ValueEventListener for KelembapanRuangan
        kelembapanRuanganRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle data change for KelembapanRuangan
                // Update your UI or process data as needed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        // Add ValueEventListener for KelembapanTanah
        kelembapanTanahRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle data change for KelembapanTanah
                // Update your UI or process data as needed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        // Add ValueEventListener for SuhuRuangan
        suhuRuanganRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle data change for SuhuRuangan
                // Update your UI or process data as needed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
}
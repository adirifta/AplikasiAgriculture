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

    private lateinit var graphView: GraphView
    private lateinit var series: LineGraphSeries<DataPoint>
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphView = findViewById(R.id.graphView)
        series = LineGraphSeries()
        graphView.addSeries(series)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Tubes")

        setupGraphView()

        // You can remove setListeners() since the button and text input are removed
    }

    private fun setupGraphView() {
        graphView.gridLabelRenderer.numHorizontalLabels = 3

        graphView.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
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
    }
}
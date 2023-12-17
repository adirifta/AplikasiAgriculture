package com.example.aplikasiagriculture

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class AnalisaData : AppCompatActivity() {

    private lateinit var IntensitasCahaya: EditText
    private lateinit var KelembapanRuangan: EditText
    private lateinit var SuhuRuangan: EditText
    private lateinit var Status: Button
    private lateinit var result: TextView
    private val url = "https://aplikasiagriculture-dae6f56127d9.herokuapp.com/Status"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analisa_data)

        IntensitasCahaya = findViewById(R.id.IntensitasCahaya)
        KelembapanRuangan = findViewById(R.id.KelembapanRuangan)
        SuhuRuangan = findViewById(R.id.SuhuRuangan)
        Status = findViewById(R.id.Status)
        result = findViewById(R.id.result)

        Status.setOnClickListener {
            // hit the API -> Volley
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val data = jsonObject.getString("Status")
                        result.text = if (data == "1") {
                            "Hujan"
                        } else {
                            "Tidak Hujan"
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    val toast = Toast.makeText(applicationContext, "Teks yang akan ditampilkan", Toast.LENGTH_SHORT)
                    toast.show()
                }) {

                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["IntensitasCahaya"] = IntensitasCahaya.text.toString()
                    params["KelembapanRuangan"] = KelembapanRuangan.text.toString()
                    params["SuhuRuangan"] = SuhuRuangan.text.toString()
                    return params
                }
            }
            val queue: RequestQueue = Volley.newRequestQueue(this@AnalisaData)
            queue.add(stringRequest)
        }
    }
}

package com.cs407.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)
        button = findViewById(R.id.ingredientsButton)

        button.setOnClickListener(){
            loadInfo("banana")
        }
    }

    private fun loadInfo(string: String){
        val volleyQueue = Volley.newRequestQueue(this)

        val url = "https://api.nal.usda.gov/fdc/v1/foods/search?query=$string&dataType=Foundation&pageSize=25&pageNumber=1&sortBy=dataType.keyword&sortOrder=asc&api_key=wHlGTLGnBEYk1fuY6y9f3tID1TosgKjlxF3CQdMf"
        var resultString = ""
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val jsonObject = JSONObject(response.toString())
                val results = jsonObject.getJSONArray("foods")
                for(i in 0..1 ){
                    val obj = results.getJSONObject(i)
                    resultString += "Name: $string: Description: ${obj.getString("description")}\n"
                    val nutrients = obj.getJSONArray("foodNutrients")
                    for(j in 0..nutrients.length() - 1){
                        val nutrient = nutrients.getJSONObject(j)
                        if(nutrient.get("nutrientName") == "Protein")
                            resultString += "Protein: ${nutrient.getString("value")}\n"
                    }
                }
                textView.text = resultString

            },
            { error ->
                // TODO: Handle error
                textView.text = "Response: %s".format(error.toString())
            }
        )

        volleyQueue.add(jsonObjectRequest)


    }
}
package com.example.smart_rozetka_app

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.example.smart_rozetka_app.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    // IP-adress ESP8266
    private val ESP_IP = "192.168.4.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use binding for switches
        binding.switchRelay1.setOnCheckedChangeListener { _, isChecked ->
            sendRelayState(1, if (isChecked) 1 else 0)
        }

        binding.switchRelay2.setOnCheckedChangeListener { _, isChecked ->
            sendRelayState(2, if (isChecked) 1 else 0)
        }
    }

    // Function to send an HTTP request
    private fun sendRelayState(relay: Int, state: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val url = "http://$ESP_IP/update?relay=$relay&state=$state&session=true"

                val request = Request.Builder()
                    .url(url)
                    .build()
                val response = client.newCall(request).execute()

                // Checking the response and outputting it to the log
                if (response.isSuccessful) {
                    val responseText = response.body?.string()
                    runOnUiThread {
                        binding.responseTextView.visibility = View.INVISIBLE
                        binding.responseTextView1.visibility = View.VISIBLE
                        binding.responseTextView2.visibility = View.GONE
                        binding.responseTextView3.visibility = View.GONE
                        binding.responseTextView1.text = "Successfully: $responseText"
                    }
                } else {
                    runOnUiThread {
                        binding.responseTextView.visibility = View.INVISIBLE
                        binding.responseTextView2.visibility = View.VISIBLE
                        binding.responseTextView1.visibility = View.GONE
                        binding.responseTextView2.text = "Error: ${response.code}"
                    }
                }
                response.close()
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    binding.responseTextView.visibility = View.INVISIBLE
                    binding.responseTextView3.visibility = View.VISIBLE
                    binding.responseTextView2.visibility = View.GONE
                    binding.responseTextView1.visibility = View.GONE
                    binding.responseTextView3.text = "Exception: ${e.message}"
                }
            }
        }
    }
}

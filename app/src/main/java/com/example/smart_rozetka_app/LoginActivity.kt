package com.example.smart_rozetka_app

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.example.smart_rozetka_app.databinding.ActivityLoginBinding
import android.content.Intent


class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val passwordInput = binding.PlainLogin
        val loginButton = binding.btnLogin

        loginButton.setOnClickListener {
            val enteredPassword = passwordInput.text.toString()
            if (enteredPassword == Constants.PASSWORD) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                binding.tvNotice.visibility = View.VISIBLE
            }
        }
    }
}
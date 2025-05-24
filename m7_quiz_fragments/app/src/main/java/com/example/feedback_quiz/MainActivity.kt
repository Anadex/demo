package com.example.feedback_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.feedback_quiz.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
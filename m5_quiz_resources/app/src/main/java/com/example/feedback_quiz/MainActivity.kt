package com.example.feedback_quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.feedback_quiz.databinding.MainActivityBinding
import com.example.feedback_quiz.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
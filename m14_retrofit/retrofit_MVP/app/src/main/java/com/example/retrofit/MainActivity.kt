package com.example.retrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UserPresenter.attachView(this)

        if (savedInstanceState == null) UserPresenter.startingView()
        else UserPresenter.loadCurrentUser()

        binding.button.setOnClickListener {
            UserPresenter.refreshUser()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserPresenter.detachView()
    }
}
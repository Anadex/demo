package com.example.room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.room.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val wordDao = (application as App).db.wordDao()
        val viewModel: MainViewModel by viewModels { MainViewModelFactory(wordDao) }

        binding.buttonAdd.setOnClickListener {
            if (!viewModel.onAddButton(binding.editText.text.toString())) Toast.makeText(
                this,
                R.string.toast,
                Toast.LENGTH_LONG
            ).show()
            binding.editText.text.clear()
        }

        binding.buttonClear.setOnClickListener {
            viewModel.onClearButton()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.allWords.collect { words ->
                    var text = ""
                    getString(R.string.word)
                    words.take(5).forEach { word ->
                        text =
                            text.plus("${getString(R.string.word)} ${word.word} - ${word.count}\r\n")
                    }
                    binding.words.text = text

                }
            }
        }


    }
}
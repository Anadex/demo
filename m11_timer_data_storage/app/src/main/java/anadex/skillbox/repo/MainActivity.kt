package anadex.skillbox.repo

import anadex.skillbox.repo.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private lateinit var repository: Repository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = Repository(this)
        binding.textView.text = repository.getText()

        binding.saveButton.setOnClickListener {
            repository.saveText(binding.editText.text.toString())
            binding.textView.text = repository.getText()
        }

        binding.clearButton.setOnClickListener {
            repository.clearText()
            binding.textView.text = ""
            binding.editText.text = null
        }
    }
}
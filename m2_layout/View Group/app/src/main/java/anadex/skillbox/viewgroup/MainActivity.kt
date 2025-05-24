package anadex.skillbox.viewgroup

import anadex.skillbox.viewgroup.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firstView.binding.topRow.text="верхняя строчка, настроенная из кода"
        binding.firstView.binding.bottomRow.text="нижняя строчка, настроенная из кода"

    }
}
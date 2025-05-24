package anadex.skillbox.timer

import anadex.skillbox.timer.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var startTimerValue = 10
        binding.progressBar.max = startTimerValue
        binding.progressBar.progress = startTimerValue

        var scope = CoroutineScope(Job() + Dispatchers.Main)

        binding.button.setOnClickListener {
            when (binding.button.text) {
                getString(R.string.startButton) -> {
                    scope = CoroutineScope(Job() + Dispatchers.Main)
                    binding.button.setText(R.string.stopButton)
                    binding.slider.isEnabled = false
                    binding.progressBar.max = startTimerValue
                    binding.progressBar.progress = startTimerValue

                    scope.launch {
                        for (n in startTimerValue - 1 downTo 0) {
                            delay(1000)
                            binding.numericView.text = n.toString()
                            binding.progressBar.progress = n
                        }

                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.taskCompleted),
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.button.setText(R.string.startButton)
                        binding.slider.isEnabled = true
                        binding.numericView.text = startTimerValue.toString()
                        binding.progressBar.progress = startTimerValue
                    }
                }

                getString(R.string.stopButton) -> {
                    binding.button.setText(R.string.startButton)
                    binding.slider.isEnabled = true
                    binding.progressBar.max = startTimerValue
                    binding.progressBar.progress = startTimerValue
                    binding.numericView.text = startTimerValue.toString()
                    scope.cancel()
                }
            }
        }

        binding.slider.addOnChangeListener { _, value, _ ->
            startTimerValue = value.toInt()
            binding.numericView.text = startTimerValue.toString()
        }
    }
}

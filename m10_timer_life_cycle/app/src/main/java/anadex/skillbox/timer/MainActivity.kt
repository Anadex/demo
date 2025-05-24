package anadex.skillbox.timer

import anadex.skillbox.timer.databinding.ActivityMainBinding
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

private const val BUTTON_TEXT = "Button Text"
private const val CURRENT_TIME = "Current time"
private const val START_TIME = "Start time"

class MainActivity : AppCompatActivity() {
    private var startTime = 10
    private var currentTime = startTime
    private val startText:String get() = getString(R.string.startButton)
    private val stopText:String get() = getString(R.string.stopButton)
    private lateinit var buttonText:String
    private var scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonText = startText

        savedInstanceState?.let {
            buttonText = it.getString(BUTTON_TEXT)!!
            startTime = it.getInt(START_TIME)
            currentTime = it.getInt(CURRENT_TIME)
        }

        binding.progressBar.max = startTime
        binding.progressBar.progress = currentTime
        binding.slider.value = startTime.toFloat()
        binding.numericView.text = currentTime.toString()
        binding.button.text = buttonText

        if (buttonText == stopText) timer(binding)

        binding.button.setOnClickListener {
            if (binding.button.text == startText) {
                binding.button.text = stopText
                timer(binding)
            } else {
                binding.button.text = startText
                timer(binding)
            }
        }

        binding.slider.addOnChangeListener { _, value, _ ->
            startTime = value.toInt()
            currentTime = startTime
            binding.numericView.text = startTime.toString()
            binding.progressBar.max = startTime
            binding.progressBar.progress = startTime
        }
    }

    private fun timer(binding: ActivityMainBinding) {

        when (binding.button.text) {
            stopText -> {
                buttonText = binding.button.text.toString()
                binding.slider.isEnabled = false

                scope.launch {
                    for (n in currentTime - 1 downTo 0) {
                        delay(1000)
                        binding.numericView.text = n.toString()
                        binding.progressBar.progress = n
                        currentTime = n
                    }

                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.taskCompleted),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.button.setText(R.string.startButton)
                    binding.slider.isEnabled = true
                    binding.numericView.text = startTime.toString()
                    binding.progressBar.progress = startTime
                    currentTime = startTime
                    buttonText = binding.button.text.toString()
                }
            }

            startText -> {
                buttonText = binding.button.text.toString()
                binding.slider.isEnabled = true
                currentTime = startTime
                binding.progressBar.progress = startTime
                binding.numericView.text = startTime.toString()
                scope.coroutineContext.cancelChildren()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(CURRENT_TIME, currentTime)
        outState.putString(BUTTON_TEXT, buttonText)
        outState.putInt(START_TIME, startTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

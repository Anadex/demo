package anadex.skillbox.passengerscounter

import anadex.skillbox.passengerscounter.databinding.ActivityMainBinding
import android.graphics.Color.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val numOfSeats = 50
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changingInfo(counter, binding)

        binding.buttonPlus.setOnClickListener {
            counter++
            changingInfo(counter, binding)
        }

        binding.buttonMinus.setOnClickListener {
            counter--
            changingInfo(counter, binding)
        }

        binding.buttonReset.setOnClickListener {
            counter = 0
            changingInfo(counter, binding)
        }
    }

    private fun changingInfo(counter: Int, binding: ActivityMainBinding) {

        binding.counter.text = counter.toString()

        when (val numOfAvailableSeats = numOfSeats - counter) {
            in Int.MIN_VALUE..0 -> {
                binding.textInfo.setTextColor(RED)
                binding.textInfo.text = getString(R.string.textInfoTooManyPassengers)
                binding.buttonMinus.isEnabled = true
                binding.buttonReset.isVisible = true
            }
            in 1 until numOfSeats -> {
                binding.textInfo.setTextColor(getColor(R.color.blue))

                val newTextInfo =
                    "${getString(R.string.textInfoHavePassengers)}: $numOfAvailableSeats"
                binding.textInfo.text = newTextInfo
                binding.buttonMinus.isEnabled = true
                binding.buttonReset.isVisible = false
            }
            else -> {
                binding.textInfo.setTextColor(getColor(R.color.green))
                binding.textInfo.text = getString(R.string.textInfoNoPassengers)
                binding.buttonMinus.isEnabled = false
                binding.buttonReset.isVisible = false
            }
        }
    }
}
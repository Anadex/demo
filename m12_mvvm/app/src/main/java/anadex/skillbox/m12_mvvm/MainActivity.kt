package anadex.skillbox.m12_mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import anadex.skillbox.m12_mvvm.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }
}
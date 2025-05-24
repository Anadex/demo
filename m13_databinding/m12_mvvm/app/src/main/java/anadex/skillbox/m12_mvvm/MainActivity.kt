package anadex.skillbox.m12_mvvm

import anadex.skillbox.m12_mvvm.ui.main.MainFragment
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

lateinit var RESOURCES: Resources

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }

        RESOURCES = applicationContext.resources
    }

}
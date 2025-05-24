package anadex.skillbox.m12_mvvm.ui.main

import kotlinx.coroutines.delay

class Repository {
    suspend fun getData(): String? {
        delay(5000)
        return listOf(null, "Кое что нашли...").random()
    }
}
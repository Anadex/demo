package anadex.skillbox.repo

import android.content.Context
import android.content.Context.MODE_PRIVATE

const val PREFERENCE = "String"
const val KEY = "Text from editText"

class Repository(context: Context) {

    private var localString: String? = null
    private val prefs = context.getSharedPreferences(PREFERENCE, MODE_PRIVATE)

    fun saveText(text: String) {
        prefs.edit().putString(KEY, text + " - из SP").apply()
        localString = text + " - из локальной переменной"
    }

    private fun getDataFromSharedPreference(): String? {
        return prefs.getString(KEY, null)
    }

    private fun getDataFromLocalVariable(): String? {
        return localString
    }

    fun clearText() {
        prefs.edit().clear().apply()
        localString = null
    }

    fun getText(): String {
        return getDataFromLocalVariable() ?: getDataFromSharedPreference() ?: ""
    }
}
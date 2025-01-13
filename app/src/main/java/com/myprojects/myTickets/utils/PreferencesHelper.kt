package com.myprojects.myTickets.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFERENCES_FILE_KEY = "com.myprojects.myTickets.PREFERENCES"
    private const val LANGUAGE_KEY = "language_key"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
    }

    // Guardar el idioma seleccionado
    fun setLanguage(context: Context, language: String) {
        val editor = getPreferences(context).edit()
        editor.putString(LANGUAGE_KEY, language)
        editor.apply()
    }

    // Obtener el idioma guardado (por defecto, español)
    fun getLanguage(context: Context): String {
        return getPreferences(context).getString(LANGUAGE_KEY, "es") ?: "es"
    }
}

package com.myprojects.myTickets.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext



object LanguageUtils{
    fun getString(context: Context, key: String, language: String): String {
        val resourceKey = "${key}_$language" // Concatenar clave con idioma (e.g., "app_name_en")
        val resId = context.resources.getIdentifier(resourceKey, "string", context.packageName)
        return if (resId != 0) context.getString(resId) else key // Retorna clave si no encuentra
    }
}


@Composable
fun getLocalizedString(name: String, language: String): String {
    val context = LocalContext.current // Obtén el contexto desde Jetpack Compose
    val resId = context.resources.getIdentifier("${name}_$language", "string", context.packageName)
    return if (resId != 0) {
        context.getString(resId)
    } else {
        throw IllegalArgumentException("Resource with name ${name}_$language not found.")
    }
}


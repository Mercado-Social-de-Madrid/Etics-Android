package net.mercadosocial.moneda.util

import androidx.appcompat.app.AppCompatDelegate

object LangUtils {


    const val DEFAULT_LANG = "es"

    @JvmStatic
    fun getCurrentLang(): String = AppCompatDelegate.getApplicationLocales().takeIf { !it.isEmpty }?.let {
        it[0]?.language
    } ?: DEFAULT_LANG
}


package net.mercadosocial.moneda.util

import androidx.appcompat.app.AppCompatDelegate

object LangUtils {

    @JvmStatic
    fun getCurrentLang(): String? = AppCompatDelegate.getApplicationLocales().takeIf { !it.isEmpty }?.let {
        it[0]?.language
    }
}


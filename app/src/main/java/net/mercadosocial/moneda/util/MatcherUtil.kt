package net.mercadosocial.moneda.util

import android.text.TextUtils
import android.util.Patterns


object MatcherUtil {

    @JvmStatic
    fun isValidEmail(target: String?) = !TextUtils.isEmpty(target)
            && Patterns.EMAIL_ADDRESS.matcher(target).matches()

    @JvmStatic
    fun isValidPhone(target: String?) = !TextUtils.isEmpty(target)
            && Patterns.PHONE.matcher(target).matches()

}
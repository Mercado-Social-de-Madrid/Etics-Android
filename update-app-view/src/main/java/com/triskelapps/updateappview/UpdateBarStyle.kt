package com.triskelapps.updateappview

import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes

data class UpdateBarStyle(
    @ColorRes val backgroundColor: Int,
    @ColorRes val foregroundElementsColor: Int,
    @StyleRes val textStyle: Int?,
)

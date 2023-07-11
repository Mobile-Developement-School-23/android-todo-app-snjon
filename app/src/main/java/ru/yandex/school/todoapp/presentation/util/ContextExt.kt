package ru.yandex.school.todoapp.presentation.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorExt(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getDrawableExt(@ColorRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

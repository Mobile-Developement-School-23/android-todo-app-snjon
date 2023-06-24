package ru.yandex.school.todoapp.presentation.util

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import ru.yandex.school.todoapp.R

fun View.makeVisible() {
    this.isVisible = true
}

fun View.makeGone() {
    this.isVisible = false
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.visibleOrGone(condition: Boolean) {
    if (condition) {
        makeVisible()
    } else {
        makeGone()
    }
}

fun View.visibleOrInvisible(condition: Boolean) {
    if (condition) {
        makeVisible()
    } else {
        makeInvisible()
    }
}

fun View.elevationOrNot(condition: Boolean) {
    if (condition) {
        ViewCompat.setElevation(this, resources.getDimension(R.dimen.toolbar_elevation_on))
    } else {
        ViewCompat.setElevation(this, resources.getDimension(R.dimen.toolbar_elevation_off))
    }
}

fun TextView.setColor(colorResId: Int) {
    val color = ContextCompat.getColor(context, colorResId)
    this.setTextColor(color)
}

fun MaterialButton.setButtonColor(condition: Boolean) {
    val textColorRes = if (condition) R.color.color_red else R.color.label_disable
    val iconTintRes = if (condition) R.color.color_red else R.color.label_disable

    val textColor = ContextCompat.getColor(context, textColorRes)
    val iconTint = ContextCompat.getColorStateList(context, iconTintRes)

    this.setTextColor(textColor)
    this.iconTint = iconTint
}
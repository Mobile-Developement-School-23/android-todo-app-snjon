package ru.yandex.school.todoapp.presentation.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
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
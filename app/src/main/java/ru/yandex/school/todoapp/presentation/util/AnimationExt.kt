package ru.yandex.school.todoapp.presentation.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import androidx.core.content.ContextCompat
import ru.yandex.school.todoapp.R

private const val ANIMATION_DURATION = 400L

fun View.animateHighlight(onAnimationEnd: () -> Unit) {
    val highlightAnimator = ValueAnimator.ofArgb(
        ContextCompat.getColor(context, R.color.color_red),
        ContextCompat.getColor(context, R.color.back_secondary)
    )

    highlightAnimator.addUpdateListener { animator ->
        setBackgroundColor(animator.animatedValue as Int)
    }

    highlightAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            return
        }

        override fun onAnimationEnd(animation: Animator) {
            onAnimationEnd()
        }

        override fun onAnimationCancel(animation: Animator) {
            return
        }

        override fun onAnimationRepeat(animation: Animator) {
            return
        }
    })

    highlightAnimator.duration = ANIMATION_DURATION
    highlightAnimator.start()
}

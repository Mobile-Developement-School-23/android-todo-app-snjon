package ru.yandex.school.todoapp.presentation.snackbar

import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.presentation.snackbar.model.SnackbarModel

private const val COUNT_DOWN_INTERVAL = 1000L
private const val MILLISECONDS_IN_SECOND = 1000L
private const val DURATION = 6000L

/**
 * A helper class for displaying Snackbar messages in a Fragment
 */
class SnackbarHost {

    private var fragment: Fragment? = null

    fun attach(fragment: Fragment) {
        this.fragment = fragment
    }

    fun detach() {
        fragment = null
    }

    fun showSnackbar(model: SnackbarModel) {
        val view = fragment?.view?.findViewById<View>(R.id.todo_list_coordinator_layout) ?: return
        val snackbar = Snackbar.make(view, model.message, Snackbar.LENGTH_INDEFINITE)

        snackbar.setActionTextColor(ContextCompat.getColor(snackbar.context, R.color.color_red))
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE

        val countDownTimer = object : CountDownTimer(DURATION, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / MILLISECONDS_IN_SECOND
                snackbar.setText(
                    snackbar.context.getString(
                        R.string.snackbar_text,
                        model.message,
                        seconds
                    )
                )
            }

            override fun onFinish() {
                snackbar.dismiss()
                model.action?.onFinished?.invoke()
            }
        }

        model.action?.let { action ->
            snackbar.setAction(snackbar.context.getString(R.string.snackbar_text_cancel)) {
                countDownTimer.cancel()
                action.onCancelled.invoke()
            }
        }

        countDownTimer.start()
        snackbar.show()
    }
}

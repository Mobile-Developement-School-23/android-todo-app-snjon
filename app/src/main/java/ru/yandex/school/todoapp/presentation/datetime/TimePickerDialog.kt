package ru.yandex.school.todoapp.presentation.datetime

import android.app.Dialog
import android.content.Context
import android.text.format.DateFormat
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.presentation.datetime.model.DateTimeModel
import java.util.Calendar

class TimePickerDialog : BaseDateTimePickerDialog() {

    companion object {
        fun newInstance(pickerCallback: ((DateTimeModel) -> Unit)): TimePickerDialog {
            return TimePickerDialog().apply {
                callback = pickerCallback
            }
        }
    }

    override fun initDialog(context: Context): Dialog {
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        return android.app.TimePickerDialog(
            context,
            R.style.DatePickerStyle,
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(context)
        )
    }
}
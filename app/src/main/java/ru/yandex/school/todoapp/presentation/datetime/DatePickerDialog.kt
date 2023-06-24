package ru.yandex.school.todoapp.presentation.datetime

import android.app.Dialog
import android.content.Context
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.presentation.datetime.model.DateTimeModel
import java.util.Calendar

class DatePickerDialog : BaseDateTimePickerDialog() {

    companion object {
        fun newInstance(pickerCallback: ((DateTimeModel) -> Unit)): DatePickerDialog {
            return DatePickerDialog().apply {
                callback = pickerCallback
            }
        }
    }

    override fun initDialog(context: Context): Dialog {
        calendar.add(Calendar.DATE, 0)

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val dialog =
            android.app.DatePickerDialog(context, R.style.DatePickerStyle, this, year, month, day)

        dialog.datePicker.minDate = calendar.timeInMillis

        return dialog
    }
}
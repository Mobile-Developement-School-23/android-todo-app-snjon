package ru.yandex.school.todoapp.presentation.datetime

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import ru.yandex.school.todoapp.presentation.datetime.model.DateTimeModel
import java.util.Calendar

abstract class BaseDateTimePickerDialog :
    DialogFragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    val calendar = Calendar.getInstance()

    open var callback: ((DateTimeModel) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return initDialog(requireContext())
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        callback?.invoke(DateTimeModel.Date(year, month, day))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        callback?.invoke(DateTimeModel.Time(hourOfDay, minute))
    }

    abstract fun initDialog(context: Context): Dialog
}


package ru.yandex.school.todoapp.presentation.util

import androidx.fragment.app.Fragment
import ru.yandex.school.todoapp.presentation.datetime.BaseDateTimePickerDialog
import ru.yandex.school.todoapp.presentation.datetime.model.DateTimeModel
import java.time.LocalDate

fun BaseDateTimePickerDialog.show(fragment: Fragment) {
    show(fragment.childFragmentManager, tag)
}

fun DateTimeModel.Date.toDate(): LocalDate {
    return LocalDate.of(year, month, day)
}
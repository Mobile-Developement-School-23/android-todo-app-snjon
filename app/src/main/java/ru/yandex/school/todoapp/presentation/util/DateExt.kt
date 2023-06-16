package ru.yandex.school.todoapp.presentation.util

import android.app.DatePickerDialog
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.yandex.school.todoapp.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.showDatePickerDialog(date: TextView) {
    val currentDate = LocalDate.now()
    val year = currentDate.year
    val month = currentDate.monthValue - 1
    val dayOfMonth = currentDate.dayOfMonth

    val datePickerDialog = DatePickerDialog(
        requireContext(), R.style.DatePickerStyle,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            date.text = formattedDate.toString().toLocalizedDate()
        },
        year,
        month,
        dayOfMonth
    )

    datePickerDialog.datePicker.minDate =
        currentDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    datePickerDialog.show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalizedDate(): String {
    val inputFormatter = DateTimeFormatterBuilder()
        .appendPattern("dd.MM.yyyy")
        .optionalStart()
        .appendPattern("MM.dd.yyyy")
        .optionalEnd()
        .toFormatter(Locale.getDefault())
    val outputFormatter = if (Locale.getDefault().language == "ru") {
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
    } else {
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())
    }
    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter)
}

fun String.convertFromLocalizedDateFormat(): String {
    val inputDateFormatRu = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    val inputDateFormatEn = SimpleDateFormat("MMMM d, yyyy", Locale("en", "US"))
    val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date: Date = when (Locale.getDefault().language) {
        "ru" -> inputDateFormatRu.parse(this) as Date
        else -> inputDateFormatEn.parse(this) as Date
    }
    return outputDateFormat.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    return currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}
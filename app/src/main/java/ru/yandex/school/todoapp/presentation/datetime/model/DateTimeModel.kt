package ru.yandex.school.todoapp.presentation.datetime.model

sealed class DateTimeModel {

    class Date(val year: Int, val month: Int, val day: Int) : DateTimeModel()

    class Time(val hourOfDay: Int, val minute: Int) : DateTimeModel()
}

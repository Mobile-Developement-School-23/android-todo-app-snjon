package ru.yandex.school.todoapp.data.database.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTypeConverter {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromDate(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}
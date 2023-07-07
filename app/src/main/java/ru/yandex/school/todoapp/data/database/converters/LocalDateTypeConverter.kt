package ru.yandex.school.todoapp.data.database.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Type converter for converting between LocalDate and LocalDateTime objects and their string representations
 */
class LocalDateTypeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Converts a LocalDate object to its string representation
     * @param value [LocalDate?]
     * @return [String?]
     */
    @TypeConverter
    fun fromDate(value: LocalDate?): String? {
        return value?.toString()
    }

    /**
     * Converts a string representation to a LocalDate object
     * @param value [String?]
     * @return [LocalDate?]
     */
    @TypeConverter
    fun toDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    /**
     * Converts a LocalDateTime object to its string representation
     * @param value [LocalDateTime?]
     * @return [String?]
     */
    @TypeConverter
    fun fromDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    /**
     * Converts a string representation to a LocalDateTime object
     * @param value [String?]
     * @return [LocalDateTime?]
     */
    @TypeConverter
    fun toDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}

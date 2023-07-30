package ru.yandex.school.todoapp.data.mapper

import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.ThemeType

/**
 * A mapper class for converting between strings and AppTheme enum
 */
class ThemeTypeMapper {

    fun mapStringToAppTheme(theme: String): ThemeType {
        return when (theme) {
            "SYSTEM" -> ThemeType.SYSTEM
            "LIGHT" -> ThemeType.LIGHT
            "DARK" -> ThemeType.DARK
            else -> ThemeType.SYSTEM
        }
    }

    fun mapAppThemeToStringRes(theme: ThemeType): Int {
        return when (theme) {
            ThemeType.SYSTEM -> R.string.settings_theme_system
            ThemeType.LIGHT  -> R.string.settings_theme_light
            ThemeType.DARK -> R.string.settings_theme_dark
        }
    }
}

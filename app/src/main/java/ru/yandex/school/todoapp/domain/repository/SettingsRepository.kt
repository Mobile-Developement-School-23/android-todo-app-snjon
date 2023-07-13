package ru.yandex.school.todoapp.domain.repository

import ru.yandex.school.todoapp.domain.model.ThemeType

/**
 * Interface representing a repository for accessing and modifying application settings
 */
interface SettingsRepository {

    suspend fun setTheme(theme: ThemeType)

    suspend fun getCurrentTheme(): ThemeType
}

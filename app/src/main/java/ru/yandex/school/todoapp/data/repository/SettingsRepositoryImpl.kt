package ru.yandex.school.todoapp.data.repository

import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.manager.ThemeManager
import ru.yandex.school.todoapp.data.mapper.ThemeTypeMapper
import ru.yandex.school.todoapp.domain.repository.SettingsRepository
import ru.yandex.school.todoapp.domain.model.ThemeType

/**
 * Implementation of SettingsRepository interface for accessing and modifying application settings
 * @param dataStorage [DataStorage]
 * @param themeManager [ThemeManager]
 * @param themeTypeMapper [ThemeTypeMapper]
 */
class SettingsRepositoryImpl(
    private val dataStorage: DataStorage,
    private val themeManager: ThemeManager,
    private val themeTypeMapper: ThemeTypeMapper
) : SettingsRepository {

    override suspend fun setTheme(theme: ThemeType) {
        dataStorage.theme = theme.toString()
        themeManager.applyTheme()
    }

    override suspend fun getCurrentTheme(): ThemeType {
        return themeTypeMapper.mapStringToAppTheme(dataStorage.theme)
    }
}

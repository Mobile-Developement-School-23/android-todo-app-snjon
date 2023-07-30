package ru.yandex.school.todoapp.data.manager

import androidx.appcompat.app.AppCompatDelegate
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.mapper.ThemeTypeMapper
import ru.yandex.school.todoapp.domain.model.ThemeType

class ThemeManager(
    private val dataStorage: DataStorage,
    private val themeTypeMapper: ThemeTypeMapper
) {

    fun applyTheme() {
        val theme = themeTypeMapper.mapStringToAppTheme(dataStorage.theme)

        when (theme) {
            ThemeType.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            ThemeType.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            ThemeType.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}

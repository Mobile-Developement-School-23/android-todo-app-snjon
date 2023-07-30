package ru.yandex.school.todoapp.presentation.settings.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.school.todoapp.data.mapper.ThemeTypeMapper
import ru.yandex.school.todoapp.domain.repository.AuthRepository
import ru.yandex.school.todoapp.domain.repository.SettingsRepository
import ru.yandex.school.todoapp.presentation.base.BaseViewModel
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator
import ru.yandex.school.todoapp.domain.model.ThemeType
import ru.yandex.school.todoapp.presentation.settings.model.SettingsScreenState

class TodoSettingsViewModel(
    private val navigator: AppNavigator,
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val themeTypeMapper: ThemeTypeMapper
) : BaseViewModel() {

    private val _settingsScreenState = MutableStateFlow(SettingsScreenState())
    val settingsScreenState = _settingsScreenState

    init {
        loadContent()
    }

    fun updateAppTheme(theme: ThemeType) {
        _settingsScreenState.update {
            SettingsScreenState(themeRes = themeTypeMapper.mapAppThemeToStringRes(theme))
        }
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    private fun loadContent() {
        viewModelScope.launch {
            _settingsScreenState.update {
                SettingsScreenState(
                    themeRes = themeTypeMapper.mapAppThemeToStringRes(
                        settingsRepository.getCurrentTheme()
                    )
                )
            }
        }
    }

    fun getUsername(): String? {
        return authRepository.getUsername()
    }

    fun changeUsername(username: String) {
        authRepository.changeUsername(username)
    }

    fun logout() {
        authRepository.logout()
        navigator.openAuthorizationScreen()
    }

    fun closeSettings() {
        navigator.backTodoList()
    }
}

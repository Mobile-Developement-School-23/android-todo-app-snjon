package ru.yandex.school.todoapp.presentation.authorization.viewmodel

import kotlinx.coroutines.delay
import ru.yandex.school.todoapp.domain.repository.AuthRepository
import ru.yandex.school.todoapp.presentation.authorization.model.TodoAuthorizationModelState
import ru.yandex.school.todoapp.presentation.authorization.viewmodel.mapper.AuthErrorMapper
import ru.yandex.school.todoapp.presentation.base.BaseViewModel
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator
import ru.yandex.school.todoapp.presentation.util.SingleLiveEvent

class TodoAuthorizationViewModel(
    private val repository: AuthRepository,
    private val navigator: AppNavigator,
    private val authErrorMapper: AuthErrorMapper,
) : BaseViewModel() {

    init {
        launchJob {
            delay(100)
            checkAuthorization()
        }
    }

    private val _todoAuthorizationState = SingleLiveEvent<TodoAuthorizationModelState>()
    val todoAuthorizationState = _todoAuthorizationState

    private val _errorLiveData = SingleLiveEvent<String>()
    val errorLiveData = _errorLiveData

    private fun checkAuthorization() {
        val isAuthorized = repository.isAuthorized()
        if (isAuthorized) {
            launchJob(
                onError = { handleAppError(it) }
            ) {
                _todoAuthorizationState.postValue(TodoAuthorizationModelState(loading = true))
                repository.getLastRevision()
                _todoAuthorizationState.postValue(TodoAuthorizationModelState(isAuthorized = true))
                setInternetMode(true)
            }
        }
    }

    fun setInternetMode(mode: Boolean) {
        repository.setAppMode(mode)
    }

    fun authorization(credentials: Pair<String, String>) {
        launchJob(
            onError = { handleAppError(it) }
        ) {
            _todoAuthorizationState.postValue(TodoAuthorizationModelState(loading = true))
            repository.checkAuth(credentials)
            _todoAuthorizationState.postValue(TodoAuthorizationModelState(isAuthorized = true))
            setInternetMode(true)
        }
    }

    fun openTodoList() {
        navigator.openTodoList()
    }

    private fun handleAppError(error: Throwable) {
        val errorMessage = authErrorMapper.map(error)
        _errorLiveData.postValue(errorMessage)
    }
}

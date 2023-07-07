package ru.yandex.school.todoapp.presentation.authorization.model

/**
 * State model for authorization in the Todo app
 */
data class TodoAuthorizationModelState(
    val isAuthorized: Boolean = false,
    val loading: Boolean = false
)

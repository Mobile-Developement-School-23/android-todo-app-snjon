package ru.yandex.school.todoapp.presentation.authorization.model

data class TodoAuthorizationModelState(
    val isAuthorized: Boolean = false,
    val loading: Boolean = false
)
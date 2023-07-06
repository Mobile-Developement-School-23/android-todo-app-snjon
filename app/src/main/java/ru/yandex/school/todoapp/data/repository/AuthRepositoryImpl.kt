package ru.yandex.school.todoapp.data.repository

import ru.yandex.school.todoapp.data.api.TodoApiService
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.model.error.ApiError
import ru.yandex.school.todoapp.domain.repository.AuthRepository


class AuthRepositoryImpl(
    private val todoApiService: TodoApiService,
    private val dataStorage: DataStorage,

    ) : AuthRepository {

    override suspend fun getLastRevision() {

        val response = todoApiService.checkAuth("Bearer ${dataStorage.token}")

        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        } else {
            dataStorage.knownRevision = response.body()?.revision ?: 0
        }
    }

    override fun getUserName(): String? {
        return dataStorage.user
    }

    override fun setAppMode(mode: Boolean) {
        dataStorage.onlineMode = mode
    }

    override suspend fun checkAuth(credentials: Pair<String, String>) {

        val user = credentials.first
        val token = credentials.second
        val response = todoApiService.checkAuth("Bearer $token")

        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        } else {
            dataStorage.token = token
            dataStorage.user = user
            dataStorage.knownRevision = response.body()?.revision ?: 0
        }
    }

    override fun isAuthorized(): Boolean {
        return dataStorage.onlineMode
    }

    override fun logout() {
        dataStorage.token = null
        dataStorage.user = null
        dataStorage.onlineMode = false
    }
}
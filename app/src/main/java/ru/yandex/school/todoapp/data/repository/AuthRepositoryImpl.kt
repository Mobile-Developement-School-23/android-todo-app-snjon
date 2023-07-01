package ru.yandex.school.todoapp.data.repository

import android.util.Log
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
            Log.e("token", "auth revision: ${dataStorage.knownRevision}")
        }
    }

    override fun getUserName(): String? {
        return dataStorage.user
    }

    override fun setAppMode(mode: Boolean) {
        dataStorage.onlineMode = mode
        Log.e("online", "setOnline: ${dataStorage.onlineMode}")
    }

    override suspend fun checkAuth(credentials: Pair<String, String>) {

        val user = credentials.first
        val token = credentials.second
        val response = todoApiService.checkAuth("Bearer $token")

        Log.e("token", "response code: ${response.code()}")
        Log.e("token", "response body: ${response.body()}")

        if (!response.isSuccessful) {
            Log.e("token", "wrong token")
            throw ApiError(response.code(), response.message())
        } else {
            dataStorage.token = token
            dataStorage.user = user
            dataStorage.knownRevision = response.body()?.revision ?: 0
            Log.e("token", "auth revision: ${dataStorage.knownRevision}")
            Log.e("token", "save token")
        }
    }

    override fun isAuthorized(): Boolean {
        return dataStorage.onlineMode
    }

    override fun logout() {
        dataStorage.token = null
        dataStorage.user = null
        dataStorage.onlineMode = false
        Log.e("token", "delete token: ${dataStorage.token}")
    }
}
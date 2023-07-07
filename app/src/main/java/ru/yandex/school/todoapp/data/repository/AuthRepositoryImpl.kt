package ru.yandex.school.todoapp.data.repository

import ru.yandex.school.todoapp.data.network.TodoApiService
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.model.error.ApiError
import ru.yandex.school.todoapp.domain.repository.AuthRepository

/**
 * Implementation of AuthRepository
 * @param todoApiService The TodoApiService instance for making API calls
 * @param dataStorage The DataStorage instance for managing app data
 */
class AuthRepositoryImpl(
    private val todoApiService: TodoApiService,
    private val dataStorage: DataStorage
) : AuthRepository {

    /**
     * Get the last known revision from the server
     * @throws ApiError if the API response is not successful
     */
    override suspend fun getLastRevision() {
        val response = todoApiService.checkAuth("Bearer ${dataStorage.token}")

        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        } else {
            dataStorage.knownRevision = response.body()?.revision ?: 0
        }
    }

    /**
     * Get the username
     * @return [String?]
     */
    override fun getUserName(): String? {
        return dataStorage.user
    }

    /**
     * Set the app mode
     * @param mode [Boolean]
     */
    override fun setAppMode(mode: Boolean) {
        dataStorage.onlineMode = mode
    }

    /**
     * Check the authentication credentials
     * @param credentials [Pair<String>]
     * @throws ApiError if the API response is not successful
     */
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

    /**
     * Check if the user is authorized
     * @return [Boolean]
     */
    override fun isAuthorized(): Boolean {
        return dataStorage.onlineMode
    }

    /**
     * Logout the user
     */
    override fun logout() {
        dataStorage.token = null
        dataStorage.user = null
        dataStorage.onlineMode = false
    }
}

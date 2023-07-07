package ru.yandex.school.todoapp.domain.repository

/**
 * Repository interface for authentication related operations
 */
interface AuthRepository {

    suspend fun checkAuth(credentials: Pair<String, String>)

    suspend fun getLastRevision()

    fun getUserName() : String?

    fun setAppMode(mode: Boolean)

    fun isAuthorized(): Boolean

    fun logout()
}

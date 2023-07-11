package ru.yandex.school.todoapp.domain.repository

/**
 * Repository interface for authentication related operations
 */
interface AuthRepository {

    suspend fun login(credentials: Pair<String, String>)

    suspend fun getLastRevision()

    fun getUserName() : String?

    fun isAuthorized(): Boolean

    fun logout()
}

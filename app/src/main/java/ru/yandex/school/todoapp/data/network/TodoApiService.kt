package ru.yandex.school.todoapp.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.yandex.school.todoapp.data.model.request.AddTodoListRequest
import ru.yandex.school.todoapp.data.model.request.AddTodoRequest
import ru.yandex.school.todoapp.data.model.response.TodoItemResponse
import ru.yandex.school.todoapp.data.model.response.TodoListResponse
import ru.yandex.school.todoapp.domain.model.TodoItem

/**
 * Retrofit API interface
 */
interface TodoApiService {

    /**
     * Checks the authorization status with the provided token
     * @param token [String]
     * @return [Response] containing [TodoListResponse]
     */
    @GET("list")
    suspend fun checkAuth(@Header("Authorization") token: String): Response<TodoListResponse>

    /**
     * Retrieves the TodoList
     * @return [Response] containing [TodoListResponse]
     */
    @GET("list")
    suspend fun getTodoList(): Response<TodoListResponse>

    /**
     * Updates the TodoList with the specified request
     * @param revision [Int]
     * @param request [AddTodoListRequest]
     * @return [Response] containing [TodoListResponse]
     */
    @PATCH("list")
    suspend fun updateTodoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: AddTodoListRequest
    ): Response<TodoListResponse>

    /**
     * Retrieves the TodoItem with the specified ID
     * @param id [String]
     * @return [TodoItem]
     */
    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): TodoItem

    /**
     * Adds a new TodoItem to the TodoList
     * @param revision [Int]
     * @param request [AddTodoRequest]
     * @return [Response] containing [TodoItemResponse]
     */
    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: AddTodoRequest
    ): Response<TodoItemResponse>

    /**
     * Updates the TodoItem with the specified ID
     * @param id [String]
     * @param revision [Int]
     * @param request [AddTodoRequest]
     * @return [Response] containing [TodoItemResponse]
     */
    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: AddTodoRequest
    ): Response<TodoItemResponse>

    /**
     * Deletes the TodoItem with the specified ID
     * @param revision [Int]
     * @param id [String]
     * @return [Response] containing [TodoItemResponse]
     */
    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoItemResponse>
}

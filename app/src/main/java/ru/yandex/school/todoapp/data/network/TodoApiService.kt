package ru.yandex.school.todoapp.data.network

import retrofit2.Response
import retrofit2.http.*
import ru.yandex.school.todoapp.data.model.request.AddTodoListRequest
import ru.yandex.school.todoapp.data.model.request.AddTodoRequest
import ru.yandex.school.todoapp.data.model.response.TodoItemResponse
import ru.yandex.school.todoapp.data.model.response.TodoListResponse
import ru.yandex.school.todoapp.domain.model.TodoItem

interface TodoApiService {
    // "Авторизация"
    @GET("list")
    suspend fun checkAuth(@Header("Authorization") token: String): Response<TodoListResponse>

    // Получить список с сервера v2
    @GET("list")
    suspend fun getTodoList(): Response<TodoListResponse>

    // Обновить список на сервере
    @PATCH("list")
    suspend fun updateTodoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: AddTodoListRequest
    ): Response<TodoListResponse>

    // Получить элемент списка
    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): TodoItem

    // Добавить элемент списка
    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: AddTodoRequest
    ): Response<TodoItemResponse>

    // Изменить элемент списка
    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: AddTodoRequest
    ): Response<TodoItemResponse>

    // Удалить элемент списка
    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<TodoItemResponse>
}

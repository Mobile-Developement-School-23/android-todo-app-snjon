package ru.yandex.school.todoapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.yandex.school.todoapp.data.api.TodoApiService
import ru.yandex.school.todoapp.data.database.dao.TodoDao
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.data.model.error.ApiError
import ru.yandex.school.todoapp.data.model.request.AddTodoListRequest
import ru.yandex.school.todoapp.data.model.request.AddTodoRequest
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl(
    private val todoDao: TodoDao,
    private val todoApiService: TodoApiService,
    private val dataStorage: DataStorage,
    private val entityMapper: TodoEntityMapper,
    private val itemsMapper: TodoItemMapper,
) : TodoItemsRepository {

    override val todoItemsFlow = todoDao.getAll()
        .map { entityMapper.map(it) }
        .flowOn(Dispatchers.Default)

    override suspend fun getTodoById(id: String): TodoItem? {
        val entity = todoDao.getTodoById(id) ?: return null

        return entityMapper.map(entity)
    }

    override suspend fun loadFromServer() {

        if (todoDao.isUnsynchronized() && dataStorage.onlineMode) {
            val items = entityMapper.map(todoDao.getTodoItems())
            saveTodoItems(items)
        }

        if (dataStorage.onlineMode) {

            val response = todoApiService.getTodoList()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            dataStorage.knownRevision = body.revision

            val list = body.data.map {
                itemsMapper.mapFromResponse(it)
            }

            todoDao.saveTodoItems(itemsMapper.map(list.map {
                it.copy(isSync = true)
            }))
        }
    }

    override suspend fun updateTodoItem(item: TodoItem) {

        todoDao.saveTodoItem(itemsMapper.map(item))

        if (dataStorage.onlineMode) {
            val newRequest = AddTodoRequest(itemsMapper.mapToRequest(dataStorage.deviceId, item))

            val response = todoApiService.updateTodoItem(item.id, dataStorage.knownRevision, newRequest)

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            todoDao.saveTodoItem(itemsMapper.map(item.copy(isSync = true)))
            dataStorage.knownRevision = body.revision
        }
    }

    override suspend fun addTodoItem(item: TodoItem) {

        todoDao.saveTodoItem(itemsMapper.map(item))

        if (dataStorage.onlineMode) {
            val newRequest = AddTodoRequest(itemsMapper.mapToRequest(dataStorage.deviceId, item))

            val response = todoApiService.addTodoItem(dataStorage.knownRevision, newRequest)

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            todoDao.saveTodoItem(itemsMapper.map(item.copy(isSync = true)))
            dataStorage.knownRevision = body.revision
        }
    }

    override suspend fun saveTodoItems(items: List<TodoItem>) {

        val list = items.map {
            itemsMapper.mapToRequest(dataStorage.deviceId, it)
        }

        if (dataStorage.onlineMode) {
            val newRequest = AddTodoListRequest(list)

            val response = todoApiService.updateTodoList(dataStorage.knownRevision, newRequest)

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            val newList = body.data.map {
                itemsMapper.mapFromResponse(it)
            }

            todoDao.saveTodoItems(itemsMapper.map(newList))
            dataStorage.knownRevision = body.revision
        }
    }

    override suspend fun deleteTodoItem(item: TodoItem) {

        todoDao.deleteTodoItem(item.id)

        if (dataStorage.onlineMode) {

            val response = todoApiService.deleteTodoItem(dataStorage.knownRevision, item.id)

            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            dataStorage.knownRevision = body.revision
        }
    }
}
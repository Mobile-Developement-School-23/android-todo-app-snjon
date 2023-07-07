package ru.yandex.school.todoapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.yandex.school.todoapp.data.network.TodoApiService
import ru.yandex.school.todoapp.data.database.dao.TodoDao
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.data.model.error.ApiError
import ru.yandex.school.todoapp.data.model.request.AddTodoListRequest
import ru.yandex.school.todoapp.data.model.request.AddTodoRequest
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

/**
 * Todo repository implementation
 * @property todoDao [TodoDao]
 * @property todoApiService [TodoApiService]
 * @property dataStorage [DataStorage]
 * @property entityMapper [TodoEntityMapper]
 * @property itemsMapper [TodoItemMapper]
 */
class TodoItemsRepositoryImpl(
    private val todoDao: TodoDao,
    private val todoApiService: TodoApiService,
    private val dataStorage: DataStorage,
    private val entityMapper: TodoEntityMapper,
    private val itemsMapper: TodoItemMapper
) : TodoItemsRepository {

    override val todoItemsFlow = todoDao.getAll()
        .map { entityMapper.map(it) }
        .flowOn(Dispatchers.Default)

    /**
     * Receives TodoItem
     * Receives todoItem from local data source
     * @param id [String]
     * @return [TodoItem?]
     */
    override suspend fun getTodoById(id: String): TodoItem? {
        val entity = todoDao.getTodoById(id) ?: return null

        return entityMapper.map(entity)
    }

    /**
     * Synchronizes list of TodoItem
     * Fetches todoItems from remote data source and pushes them to local data source
     * Then pushes merged local todoItems to remote data source
     */
    override suspend fun loadFromServer() {
        if (dataStorage.onlineMode) {

            val response = todoApiService.getTodoList()
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            dataStorage.knownRevision = body.revision

            val remoteItems = body.data.map {
                itemsMapper.mapFromResponse(it)
            }

            if (!dataStorage.isSync) {
                val unsyncItems = entityMapper.map(todoDao.getUnsyncTodos())
                val mergeItems = unsyncItems + remoteItems
                saveTodoItems(mergeItems)
            }

            val localItems = entityMapper.map(todoDao.getTodoItems())
            val removedItems = localItems.filterNot { remoteItems.contains(it) }
            val addItems = remoteItems.filterNot { localItems.contains(it) }
            val modifiedItems = remoteItems.filter { remoteItem ->
                val matchingLocalItem = localItems.find { localItem ->
                    localItem.id == remoteItem.id
                }
                matchingLocalItem?.modifiedAt != remoteItem.modifiedAt && matchingLocalItem != null
            }

            if (dataStorage.isSync) {
                if (removedItems.isNotEmpty()) {
                    todoDao.deleteTodoItems(itemsMapper.map(removedItems))
                }

                if (addItems.isNotEmpty()) {
                    todoDao.saveTodoItems(itemsMapper.map(remoteItems.map {
                        it.copy(isSync = true)
                    }))
                }

                if (modifiedItems.isNotEmpty()) {
                    todoDao.saveTodoItems(itemsMapper.map(modifiedItems.map {
                        it.copy(isSync = true)
                    }))
                }
            }

            dataStorage.isSync = true
        }
    }

    /**
     * Updates TodoItem
     * Updates TodoItem from local data source and pushes it to remote data source
     * @param item [TodoItem]
     * @return [Boolean]
     */
    override suspend fun updateTodoItem(item: TodoItem): Boolean {
        todoDao.saveTodoItem(itemsMapper.map(item))
        dataStorage.isSync = false

        if (dataStorage.onlineMode) {
            getLastRevision()

            val request = AddTodoRequest(itemsMapper.mapToRequest(dataStorage.deviceId, item))
            val response =
                todoApiService.updateTodoItem(item.id, dataStorage.knownRevision, request)
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            todoDao.saveTodoItem(itemsMapper.map(item.copy(isSync = true)))
            dataStorage.isSync = true
            dataStorage.knownRevision = body.revision
        }
        return true
    }

    /**
     * Add TodoItem
     * Adds TodoItem to local data source and pushes it to remote data source
     * @param item [TodoItem]
     * @return [Boolean]
     */
    override suspend fun addTodoItem(item: TodoItem): Boolean {
        todoDao.saveTodoItem(itemsMapper.map(item))
        dataStorage.isSync = false

        if (dataStorage.onlineMode) {
            getLastRevision()

            val request = AddTodoRequest(itemsMapper.mapToRequest(dataStorage.deviceId, item))
            val response = todoApiService.addTodoItem(dataStorage.knownRevision, request)
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            todoDao.saveTodoItem(itemsMapper.map(item.copy(isSync = true)))
            dataStorage.isSync = true
            dataStorage.knownRevision = body.revision
        }
        return true
    }

    /**
     * Saves list of TodoItem
     * Saves todoItems from local data source and pushes it to remote data source
     * @param items [List<TodoItem>]
     */
    override suspend fun saveTodoItems(items: List<TodoItem>) {
        val savedItems = items.map {
            itemsMapper.mapToRequest(dataStorage.deviceId, it)
        }

        if (dataStorage.onlineMode) {
            getLastRevision()

            val request = AddTodoListRequest(savedItems)
            val response = todoApiService.updateTodoList(dataStorage.knownRevision, request)
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            val newItems = body.data.map {
                itemsMapper.mapFromResponse(it)
            }

            todoDao.saveTodoItems(itemsMapper.map(newItems.map { it.copy(isSync = true) }))
            dataStorage.knownRevision = body.revision
        }
    }

    /**
     * Deletes TodoItem
     * Deletes todoItem from local data source and pushes it to remote data source
     * @param item [TodoItem]
     * @return [Boolean]
     */
    override suspend fun deleteTodoItem(item: TodoItem): Boolean {
        todoDao.deleteTodoItem(item.id)
        dataStorage.isSync = false

        if (dataStorage.onlineMode) {
            getLastRevision()

            val response = todoApiService.deleteTodoItem(dataStorage.knownRevision, item.id)
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                throw ApiError(response.code(), response.message())
            }

            dataStorage.knownRevision = body.revision
            dataStorage.isSync = true
        }
        return true
    }

    /**
     * Receives knownRevision
     * Receives last knownRevision from remote data source and pushes it to local data source
     */
    override suspend fun getLastRevision() {
        val response = todoApiService.checkAuth("Bearer ${dataStorage.token}")

        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        } else {
            dataStorage.knownRevision = response.body()?.revision ?: 0
        }
    }
}

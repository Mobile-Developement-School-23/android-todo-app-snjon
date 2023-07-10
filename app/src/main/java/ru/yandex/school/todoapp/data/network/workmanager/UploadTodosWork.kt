package ru.yandex.school.todoapp.data.network.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent
import ru.yandex.school.todoapp.data.database.dao.TodoDao
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.data.model.request.AddTodoListRequest
import ru.yandex.school.todoapp.data.network.TodoApiService

/**
 * Worker class for uploading todos to the server
 * @property context The application context
 * @property workerParams The worker parameters
 */
class UploadTodosWork(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val todoDao: TodoDao by KoinJavaComponent.inject(TodoDao::class.java)
    private val todoApiService: TodoApiService by KoinJavaComponent.inject(TodoApiService::class.java)
    private val entityMapper: TodoEntityMapper by KoinJavaComponent.inject(TodoEntityMapper::class.java)
    private val itemsMapper: TodoItemMapper by KoinJavaComponent.inject(TodoItemMapper::class.java)
    private val dataStorage: DataStorage by KoinJavaComponent.inject(DataStorage::class.java)

    override suspend fun doWork(): Result {
        try {
            if (dataStorage.token != null) {
                val response = todoApiService.getTodoList()
                dataStorage.knownRevision = response.body()?.revision ?: 0

                val items = entityMapper.map(todoDao.getTodoItems()).map {
                    itemsMapper.mapToRequest(dataStorage.deviceId, it)
                }
                val request = AddTodoListRequest(items)
                todoApiService.updateTodoList(dataStorage.knownRevision, request)
            }
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}

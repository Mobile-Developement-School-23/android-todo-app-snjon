package ru.yandex.school.todoapp.data.api.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import ru.yandex.school.todoapp.data.api.TodoApiService
import ru.yandex.school.todoapp.data.database.dao.TodoDao
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.data.model.request.AddTodoListRequest

class UploadTodosWork(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val todoDao: TodoDao by KoinJavaComponent.inject(TodoDao::class.java)
    private val todoApiService: TodoApiService by KoinJavaComponent.inject(TodoApiService::class.java)
    private val entityMapper: TodoEntityMapper by KoinJavaComponent.inject(TodoEntityMapper::class.java)
    private val itemsMapper: TodoItemMapper by KoinJavaComponent.inject(TodoItemMapper::class.java)
    private val dataStorage: DataStorage by KoinJavaComponent.inject(DataStorage::class.java)

    override fun doWork(): Result {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                if (dataStorage.token != null) {
                    val response = todoApiService.checkAuth("Bearer ${dataStorage.token}")
                    dataStorage.knownRevision = response.body()?.revision ?: 0

                    if (todoDao.isUnsynchronized()) {
                        val items = entityMapper.map(todoDao.getTodoItems()).map {
                            itemsMapper.mapToRequest(dataStorage.deviceId, it)
                        }
                        val request = AddTodoListRequest(items)
                        todoApiService.updateTodoList(dataStorage.knownRevision, request)
                    }
                }
            }

        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}
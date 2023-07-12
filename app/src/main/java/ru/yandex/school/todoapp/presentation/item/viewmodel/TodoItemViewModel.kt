package ru.yandex.school.todoapp.presentation.item.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.base.BaseViewModel
import ru.yandex.school.todoapp.presentation.datetime.model.DateTimeModel
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.ItemErrorMapper
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.TodoItemDateMapper
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator
import ru.yandex.school.todoapp.presentation.util.toDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class TodoItemViewModel(
    private val todoItemId: String?,
    private val navigator: AppNavigator,
    private val repository: TodoItemsRepository,
    private val dateMapper: TodoItemDateMapper,
    private val itemErrorMapper: ItemErrorMapper
) : BaseViewModel() {

    private val _todoItemScreenState = MutableStateFlow(TodoItemScreenState())
    val todoItemScreenState = _todoItemScreenState

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private var todoItem: TodoItem = runBlocking { loadTodoItem() }

    init {
        loadContent()
    }

    private fun loadContent() {
        _todoItemScreenState.update {
            TodoItemScreenState(
                text = todoItem.text,
                priorityRes = todoItem.priority.titleRes,
                deadlineDate = todoItem.deadline?.let { dateMapper.map(it) },
                modifiedDate = todoItem.modifiedAt?.let { dateMapper.map(it) }
            )
        }
    }

    fun updateTodoItemText(text: String) {
        todoItem = todoItem.copy(text = text)
        _todoItemScreenState.update { it.copy(text = text) }
    }

    fun updateTodoItemPriority(priority: TodoItemPriority) {
        todoItem = todoItem.copy(priority = priority)
        _todoItemScreenState.update { it.copy(priorityRes = priority.titleRes) }
    }

    fun updateDeadlineDate(dateTimeModel: DateTimeModel) {
        val date = (dateTimeModel as? DateTimeModel.Date)?.toDate() ?: return

        todoItem = todoItem.copy(deadline = date)
        _todoItemScreenState.update { it.copy(deadlineDate = dateMapper.map(date)) }
    }

    fun onDeadlineDateActivate(isActive: Boolean) {
        if (isActive) {
            val deadlineDate = todoItem.deadline ?: LocalDate.now()
            val deadlineDateUi = deadlineDate?.let { dateMapper.map(it) }

            todoItem = todoItem.copy(deadline = deadlineDate)
            _todoItemScreenState.update { it.copy(deadlineDate = deadlineDateUi) }
        } else {
            todoItem = todoItem.copy(deadline = null)
            _todoItemScreenState.update { it.copy(deadlineDate = null) }
        }
    }

    private suspend fun loadTodoItem(): TodoItem {
        return if (todoItemId == null) {
            TodoItem.empty
        } else {
            repository.getTodoById(todoItemId) ?: TodoItem.empty
        }
    }

    fun addTodoItem() {
        val isCreating = todoItemId == null
        val currentDate = LocalDate.now()
        val currentDateTime = LocalDateTime.now().withNano(0)

        launchJob(
            onError = { handleAppError(it) },
            isGlobalJob = true
        )
        {
            if (isCreating) {
                repository.addTodoItem(
                    todoItem.copy(
                        id = UUID.randomUUID().toString(),
                        createAt = currentDate,
                        modifiedAt = currentDateTime
                    )
                )
            } else {
                repository.updateTodoItem(
                    todoItem.copy(
                        modifiedAt = currentDateTime,
                        isSync = false
                    )
                )
            }
        }
        closeTodoItem()
    }

    fun closeTodoItem() {
        navigator.backTodoList()
    }

    fun deleteTodoItem() {
        launchJob(
            onError = { handleAppError(it) }
        ) {
            repository.updateTodoItemHiddenStatus(todoItem.id, true)
        }
        closeTodoItem()
    }

    private fun handleAppError(error: Throwable) {
        val errorMessage = itemErrorMapper.map(error)
        _errorLiveData.postValue(errorMessage)
    }
}

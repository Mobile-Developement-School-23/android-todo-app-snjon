package ru.yandex.school.todoapp.presentation.list.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.repository.AuthRepository
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.base.BaseViewModel
import ru.yandex.school.todoapp.presentation.list.model.TodoListScreenState
import ru.yandex.school.todoapp.presentation.list.viewmodel.mapper.ListErrorMapper
import ru.yandex.school.todoapp.presentation.list.viewmodel.mapper.TodoListItemMapper
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator
import ru.yandex.school.todoapp.presentation.snackbar.SnackbarHost
import ru.yandex.school.todoapp.presentation.snackbar.model.SnackbarAction
import ru.yandex.school.todoapp.presentation.snackbar.model.SnackbarModel
import ru.yandex.school.todoapp.presentation.util.SingleLiveEvent
import java.time.LocalDateTime

class TodoListViewModel(
    private val repository: TodoItemsRepository,
    private val authRepository: AuthRepository,
    private val todoListItemMapper: TodoListItemMapper,
    private val navigator: AppNavigator,
    private val listErrorMapper: ListErrorMapper,
    private val snackbarHost: SnackbarHost
) : BaseViewModel() {

    init {
        loadTodos()
    }

    private val _errorLiveData = SingleLiveEvent<String>()
    val errorLiveData = _errorLiveData

    private val _todosLoadedEvent = SingleLiveEvent<Unit>()
    val todosLoadedEvent = _todosLoadedEvent

    private val _todoDeletedEvent = SingleLiveEvent<TodoItem?>()
    val todoDeletedEvent = _todoDeletedEvent

    val todoItemsFlow = repository.todoItemsFlow
    val todoListItemsState = MutableStateFlow(TodoListScreenState.empty)

    private var todoItems: List<TodoItem> = emptyList()

    fun refreshList(items: List<TodoItem>) {
        todoItems = items
        launchJob {
            todoListItemsState.update { previousState ->
                val completedCount: Int
                val shouldShowCompleted = previousState.isCompletedShowed

                val itemsModels = items
                    .also { completedCount = it.filter { it.isCompleted }.size }
                    .filter { item -> filterVisibleItem(shouldShowCompleted, item) }
                    .map { todoListItemMapper.map(it) }

                TodoListScreenState(
                    listItems = itemsModels,
                    completedCount = completedCount,
                    isCompletedShowed = previousState.isCompletedShowed
                )
            }
        }
    }

    fun loadTodos() {
        launchJob(
            onError = {
                handleAppError(it)
            }
        ) {
            repository.loadFromServer()
            _todosLoadedEvent.postValue(Unit)
        }
    }

    fun checkDeleteTodo() {
        launchJob(
            onError = { handleAppError(it) }
        ) {
            val todoItem = repository.getHiddenTodo()
            if (todoItem != null) {
                _todoDeletedEvent.postValue(todoItem)
            }
        }
    }

    private fun filterVisibleItem(shouldShowCompleted: Boolean, item: TodoItem): Boolean {
        return if (shouldShowCompleted) {
            true
        } else {
            item.isCompleted.not()
        }
    }

    fun createNewTodo() {
        navigator.openTodoItem()
    }

    fun openTodoItemInfo(todoItem: TodoItem) {
        navigator.openTodoItem(todoItem)
    }

    fun checkTodoItem(todoItem: TodoItem) {
        val checkedItem = todoItem.copy(
            isCompleted = todoItem.isCompleted.not(),
            isSync = false,
            modifiedAt = LocalDateTime.now().withNano(0)
        )
        launchJob(
            onError = { handleAppError(it) }
        ) {
            repository.updateTodoItem(checkedItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        _todoDeletedEvent.value = null
        changeTodoItemHiddenStatus(todoItem.id, true)

        snackbarHost.showSnackbar(
            SnackbarModel(
                message = todoItem.text,
                action = SnackbarAction(
                    onCancelled = {
                        changeTodoItemHiddenStatus(todoItem.id, false)
                    },
                    onFinished = {
                        launchJob {
                            repository.deleteTodoItem(todoItem)
                        }
                    }
                )
            )
        )
    }

    private fun changeTodoItemHiddenStatus(id: String, status: Boolean) {
        launchJob(
            onError = { handleAppError(it) }
        ) {
            repository.updateTodoItemHiddenStatus(id, status)
        }
    }

    fun changeCompletedTodosVisibility() {
        todoListItemsState.update { it.copy(isCompletedShowed = it.isCompletedShowed.not()) }
        refreshList(todoItems)
    }

    fun actionOnItem(todoItem: TodoItem, resId: Int) {
        when (resId) {
            R.id.menu_action_completed -> checkTodoItem(todoItem)
            R.id.menu_action_edit -> openTodoItemInfo(todoItem)
            R.id.menu_action_delete -> deleteTodoItem(todoItem)
        }
    }

    fun getUserName(): String? {
        return authRepository.getUsername()
    }

    fun isAuthorized(): Boolean {
        return authRepository.isAuthorized()
    }

    fun openSettings() {
        navigator.openSettings()
    }

    private fun handleAppError(error: Throwable) {
        val errorMessage = listErrorMapper.map(error)
        _errorLiveData.postValue(errorMessage)
    }
}

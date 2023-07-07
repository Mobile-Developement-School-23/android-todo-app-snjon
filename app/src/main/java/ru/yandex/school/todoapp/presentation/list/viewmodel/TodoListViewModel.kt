package ru.yandex.school.todoapp.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import java.time.LocalDateTime

class TodoListViewModel(
    private val repository: TodoItemsRepository,
    private val authRepository: AuthRepository,
    private val todoListItemMapper: TodoListItemMapper,
    private val navigator: AppNavigator,
    private val listErrorMapper: ListErrorMapper
) : BaseViewModel() {

    init {
        loadTodos()
    }

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val _todosLoadedEvent = MutableLiveData<Unit>()
    val todosLoadedEvent: LiveData<Unit> = _todosLoadedEvent

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
        launchJob(
            onError = { handleAppError(it) }
        ) {
            repository.deleteTodoItem(todoItem)
        }
    }

    fun changeCompletedTodosVisibility() {
        todoListItemsState.update { it.copy(isCompletedShowed = it.isCompletedShowed.not()) }
        refreshList(todoItems)
    }

    fun actionOnItem(todoItem: TodoItem, resId: Int) {
        when (resId) {
            R.id.menu_action_completed -> {
                checkTodoItem(todoItem)
            }

            R.id.menu_action_edit -> {
                openTodoItemInfo(todoItem)
            }

            R.id.menu_action_delete -> {
                deleteTodoItem(todoItem)
            }
        }
    }

    fun getUserName(): String? {
        return authRepository.getUserName()
    }

    fun isAuthorized(): Boolean {
        return authRepository.isAuthorized()
    }

    fun logout() {
        authRepository.logout()
        navigator.openAuthorizationScreen()
    }

    private fun handleAppError(error: Throwable) {
        val errorMessage = listErrorMapper.map(error)
        _errorLiveData.postValue(errorMessage)
    }
}
package ru.yandex.school.todoapp.presentation.item

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState

class TodoItemViewModel(
    private val todoItemId: String?,
    private val repository: TodoItemsRepository
) : ViewModel() {

    val todoItemScreenState = MutableStateFlow(TodoItemScreenState(todoItemId))
}
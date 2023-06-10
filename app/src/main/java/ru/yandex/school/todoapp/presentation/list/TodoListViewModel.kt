package ru.yandex.school.todoapp.presentation.list

import androidx.lifecycle.ViewModel
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

class TodoListViewModel(private val repository: TodoItemsRepository) : ViewModel() {
}
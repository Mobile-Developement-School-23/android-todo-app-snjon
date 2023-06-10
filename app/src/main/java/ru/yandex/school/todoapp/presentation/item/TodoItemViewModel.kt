package ru.yandex.school.todoapp.presentation.item

import androidx.lifecycle.ViewModel
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

class TodoItemViewModel(private val repository: TodoItemsRepository) : ViewModel() {
}
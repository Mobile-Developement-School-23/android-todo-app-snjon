package ru.yandex.school.todoapp.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl : TodoItemsRepository {

    private var items = MutableStateFlow(
        mutableMapOf(
            "1" to TodoItem(
                id = "1",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                createDate = ""
            ),
            "2" to TodoItem(
                id = "2",
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как отображается текст нашего дела",
                priority = TodoItemPriority.HIGH,
                isCompleted = false,
                createDate = ""
            ),
            "3" to TodoItem(
                id = "3",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                createDate = ""
            ),
            "4" to TodoItem(
                id = "4",
                text = "Сделать что-то",
                priority = TodoItemPriority.LOW,
                isCompleted = false,
                createDate = ""
            ),
            "5" to TodoItem(
                id = "5",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                createDate = ""
            ),
            "6" to TodoItem(
                id = "6",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                createDate = ""
            ),
        )
    )

    override fun getTodoItems(): StateFlow<Map<String, TodoItem>> {
        return items.asStateFlow()
    }

    override fun saveTodoItem(item: TodoItem) {
        items.getAndUpdate {
            it.apply { this[item.id] = item }
        }
    }

    override fun deleteTodoItem(item: TodoItem) {
        items.getAndUpdate {
            it.apply { this.remove(item.id) }
        }
    }
}
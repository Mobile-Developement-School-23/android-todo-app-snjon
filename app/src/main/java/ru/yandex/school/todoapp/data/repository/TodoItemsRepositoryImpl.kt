package ru.yandex.school.todoapp.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import java.time.LocalDate

class TodoItemsRepositoryImpl : TodoItemsRepository {
    private var items = MutableStateFlow(
        mutableMapOf(
            "1" to TodoItem(
                id = "1",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                createAt = LocalDate.of(2023, 5, 1)
            ),
            "2" to TodoItem(
                id = "2",
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как отображается текст нашего дела",
                priority = TodoItemPriority.HIGH,
                isCompleted = false,
                deadline = LocalDate.of(2023, 6, 2),
                createAt = LocalDate.of(2023, 5, 2)
            ),
            "3" to TodoItem(
                id = "3",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                deadline = LocalDate.of(2023, 6, 1),
                createAt = LocalDate.of(2023, 5, 3)
            ),
            "4" to TodoItem(
                id = "4",
                text = "Сделать что-то",
                priority = TodoItemPriority.LOW,
                isCompleted = false,
                deadline = LocalDate.of(2023, 6, 5),
                createAt = LocalDate.of(2023, 5, 4)
            ),
            "5" to TodoItem(
                id = "5",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = false,
                deadline = LocalDate.of(2023, 6, 10),
                createAt = LocalDate.of(2023, 5, 5)
            ),
            "6" to TodoItem(
                id = "6",
                text = "Сделать что-то",
                priority = TodoItemPriority.DEFAULT,
                isCompleted = true,
                deadline = LocalDate.of(2023, 6, 10),
                createAt = LocalDate.of(2023, 5, 6)
            ),
        )
    )

    override fun getTodoItems(): StateFlow<Map<String, TodoItem>> {
        return items.asStateFlow()
    }

    override fun saveTodoItem(item: TodoItem) {

        if (item.id == "0") {
            val currentItems = items.value.toMutableMap()
            val lastId = currentItems.values.lastOrNull()?.id ?: "0"
            val nextId = (lastId.toInt() + 1).toString()
            val postWithId = item.copy(id = nextId)
            currentItems[nextId] = postWithId
            items.value = currentItems
            return
        }

        items.getAndUpdate {
            it.apply { this[item.id] = item }
        }
    }

    override fun deleteTodoItem(item: TodoItem) {
        items.getAndUpdate {
            it.apply { this.remove(item.id) }
        }
    }

    override fun getTodoById(id: String): TodoItem? {
        return items.value[id]
    }
}
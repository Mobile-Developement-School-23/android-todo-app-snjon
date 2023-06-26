package ru.yandex.school.todoapp.data.repository

import ru.yandex.school.todoapp.data.database.dao.TodoDao
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl(
    private val dao: TodoDao,
    private val entityMapper: TodoEntityMapper,
    private val itemsMapper: TodoItemMapper,
) : TodoItemsRepository {

    override suspend fun getTodoItems(): List<TodoItem> {
        val entity =  dao.getTodoItems()

        return entityMapper.map(entity)
    }

    override suspend fun getTodoById(id: String): TodoItem? {
        val entity = dao.getTodoById(id) ?: return null

        return entityMapper.map(entity)
    }

    override suspend fun saveTodoItem(item: TodoItem) {
        dao.saveTodoItem(itemsMapper.map(item))
    }

    override suspend fun saveTodoItems(items: List<TodoItem>) {
        dao.saveTodoItems(itemsMapper.map(items))
    }

    override suspend fun deleteTodoItem(item: TodoItem) {
        dao.deleteTodoItem(item.id)
    }
}
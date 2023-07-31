package ru.yandex.school.todoapp.presentation.item.model

import ru.yandex.school.todoapp.R

/**
 * Represents the state of a TodoItem screen
 */
sealed interface TodoItemScreenState {
    data class Content(
        val text: String = "",
        val priorityRes: Int = R.string.todo_item_view_priority_default,
        val deadlineDate: String? = null,
        val modifiedDate: String? = null,
        val loading: Boolean = false
    ) : TodoItemScreenState

    object Loading : TodoItemScreenState

    fun asContentOrNull(): Content? {
        return if (this !is Content) {
            null
        } else {
            this
        }
    }
}

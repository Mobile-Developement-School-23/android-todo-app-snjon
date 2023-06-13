package ru.yandex.school.todoapp.presentation.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem

const val KEY_TODO_ITEM_ID = "KEY_TODO_ID"

class AppNavigator {

    private var appNavController: NavController? = null

    fun attach(navController: NavController) {
        appNavController = navController
    }

    fun detach() {
        appNavController = null
    }

    fun openTodoItem(todoItem: TodoItem? = null) {
        appNavController?.navigate(
            R.id.open_todo_item,
            bundleOf(KEY_TODO_ITEM_ID to todoItem?.id)
        )
    }
}
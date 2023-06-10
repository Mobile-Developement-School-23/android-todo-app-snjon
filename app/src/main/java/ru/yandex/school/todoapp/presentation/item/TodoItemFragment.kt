package ru.yandex.school.todoapp.presentation.item

import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yandex.school.todoapp.R

class TodoItemFragment : Fragment(R.layout.fragment_todo_item) {

    private val viewModel: TodoItemViewModel by viewModel()
}
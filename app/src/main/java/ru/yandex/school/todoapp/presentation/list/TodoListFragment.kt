package ru.yandex.school.todoapp.presentation.list

import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.yandex.school.todoapp.R

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private val viewModel: TodoListViewModel by viewModel()
}
package ru.yandex.school.todoapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yandex.school.todoapp.data.repository.TodoItemsRepositoryImpl
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.item.TodoItemViewModel
import ru.yandex.school.todoapp.presentation.list.TodoListViewModel

val appModule = module {

    viewModel { TodoItemViewModel(get()) }
    viewModel { TodoListViewModel(get()) }
    single<TodoItemsRepository> { TodoItemsRepositoryImpl() }
}
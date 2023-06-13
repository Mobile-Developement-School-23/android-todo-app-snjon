package ru.yandex.school.todoapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yandex.school.todoapp.data.repository.TodoItemsRepositoryImpl
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.item.TodoItemViewModel
import ru.yandex.school.todoapp.presentation.list.TodoListViewModel
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator

val appModule = module {

    viewModel { (todoId: String) -> TodoItemViewModel(todoId, get()) }
    viewModel { TodoListViewModel(get(), get()) }
    single { AppNavigator() }
    single<TodoItemsRepository> { TodoItemsRepositoryImpl() }
}
package ru.yandex.school.todoapp.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yandex.school.todoapp.data.database.AppDatabase
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.data.repository.TodoItemsRepositoryImpl
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.item.viewmodel.TodoItemViewModel
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.TodoItemDateMapper
import ru.yandex.school.todoapp.presentation.list.viewmodel.TodoListViewModel
import ru.yandex.school.todoapp.presentation.list.viewmodel.mapper.TodoListItemMapper
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator

val appModule = module {

    viewModel { (todoId: String) -> TodoItemViewModel(todoId, get(), get(), get()) }
    viewModel { TodoListViewModel(get(), get(), get()) }

    factory { TodoListItemMapper(androidContext(), get()) }
    factory { TodoItemDateMapper(get()) }

    single { AppNavigator() }

    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().todoDao() }

    single<TodoItemsRepository> { TodoItemsRepositoryImpl(get(), get(), get()) }
    factory { TodoEntityMapper() }
    factory { TodoItemMapper() }
}
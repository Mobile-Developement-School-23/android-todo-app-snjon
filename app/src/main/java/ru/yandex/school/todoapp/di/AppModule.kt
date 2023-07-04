package ru.yandex.school.todoapp.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yandex.school.todoapp.BuildConfig
import ru.yandex.school.todoapp.data.api.TodoApiService
import ru.yandex.school.todoapp.data.api.interceptor.AuthInterceptor
import ru.yandex.school.todoapp.data.database.AppDatabase
import ru.yandex.school.todoapp.data.datastore.DataStorage
import ru.yandex.school.todoapp.data.mapper.TodoEntityMapper
import ru.yandex.school.todoapp.data.mapper.TodoItemMapper
import ru.yandex.school.todoapp.data.provider.DatabaseProvider
import ru.yandex.school.todoapp.data.provider.OkHttpProvider
import ru.yandex.school.todoapp.data.repository.AuthRepositoryImpl
import ru.yandex.school.todoapp.data.repository.TodoItemsRepositoryImpl
import ru.yandex.school.todoapp.domain.repository.AuthRepository
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.authorization.viewmodel.TodoAuthorizationViewModel
import ru.yandex.school.todoapp.presentation.authorization.viewmodel.mapper.AuthErrorMapper
import ru.yandex.school.todoapp.presentation.item.viewmodel.TodoItemViewModel
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.ItemErrorMapper
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.TodoItemDateMapper
import ru.yandex.school.todoapp.presentation.list.viewmodel.TodoListViewModel
import ru.yandex.school.todoapp.presentation.list.viewmodel.mapper.ListErrorMapper
import ru.yandex.school.todoapp.presentation.list.viewmodel.mapper.TodoListItemMapper
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator

val appModule = module {

    viewModel { (todoId: String) -> TodoItemViewModel(todoId, get(), get(), get(), get()) }
    viewModel { TodoListViewModel(get(), get(), get(), get(), get()) }
    viewModel { TodoAuthorizationViewModel(get(), get(), get()) }

    factory { TodoListItemMapper(androidContext(), get()) }
    factory { TodoItemDateMapper(get()) }

    single { AppNavigator() }

    single { DatabaseProvider().getInstance(androidContext()) }
    single { get<AppDatabase>().todoDao() }

    single<TodoItemsRepository> { TodoItemsRepositoryImpl(get(), get(), get(), get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    single { AuthInterceptor(get()) }
    single { OkHttpProvider(get()).provide() }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApiService::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("data_storage", Context.MODE_PRIVATE)
    }

    single { DataStorage(get()) }

    factory { TodoEntityMapper() }
    factory { TodoItemMapper() }
    factory { AuthErrorMapper(get()) }
    factory { ItemErrorMapper(get()) }
    factory { ListErrorMapper(get()) }
}
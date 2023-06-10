package ru.yandex.school.todoapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yandex.school.todoapp.di.appModule

class TodoAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TodoAppApplication)
            modules(appModule)
        }
    }
}
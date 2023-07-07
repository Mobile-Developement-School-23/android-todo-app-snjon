package ru.yandex.school.todoapp.data.provider

import android.content.Context
import androidx.room.Room
import ru.yandex.school.todoapp.data.database.AppDatabase
import ru.yandex.school.todoapp.data.database.AppDatabase.Companion.DATABASE_NAME

class DatabaseProvider {

    fun getInstance(context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}

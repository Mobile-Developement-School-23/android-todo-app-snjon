package ru.yandex.school.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.yandex.school.todoapp.data.database.AppDatabase.Companion.DATABASE_VERSION
import ru.yandex.school.todoapp.data.database.dao.TodoDao
import ru.yandex.school.todoapp.data.model.database.TodoEntity

/**
 * The Room Database that contains the todoItems table
 */
@Database(entities = [TodoEntity::class], version = DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "app.db"
    }
}

package ru.yandex.school.todoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yandex.school.todoapp.data.model.database.TodoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the SQLite todoItems table
 */
@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoEntity WHERE hidden = 0 ORDER BY modifiedAt DESC")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM TodoEntity ORDER BY modifiedAt DESC")
    fun getTodoItems(): List<TodoEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM TodoEntity WHERE isSync = 0 LIMIT 1)")
    fun isUnsynchronized(): Boolean

    @Query("SELECT * FROM TodoEntity WHERE isSync = 0")
    fun getUnsyncTodos(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodoItem(todoItem: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodoItems(todos: List<TodoEntity>)

    @Query("DELETE FROM TodoEntity WHERE id = :id")
    suspend fun deleteTodoItem(id: String)

    @Delete
    suspend fun deleteTodoItems(todos: List<TodoEntity>)

    @Query("SELECT * FROM TodoEntity WHERE id = :id")
    suspend fun getTodoById(id: String): TodoEntity?

    @Query("UPDATE TodoEntity SET hidden = :hidden WHERE id = :id")
    suspend fun updateTodoItemHiddenStatus(id: String, hidden: Boolean)

    @Query("SELECT * FROM TodoEntity WHERE hidden = 1 LIMIT 1")
    suspend fun getHiddenTodoItem(): TodoEntity?
}

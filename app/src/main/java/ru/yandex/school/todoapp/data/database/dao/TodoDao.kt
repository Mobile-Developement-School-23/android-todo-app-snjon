package ru.yandex.school.todoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yandex.school.todoapp.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM TodoEntity ORDER BY modifiedAt DESC")
    fun getTodoItems(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodoItem(todoItem: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodoItems(todos: List<TodoEntity>)

    @Query("DELETE FROM TodoEntity WHERE id = :id")
    suspend fun deleteTodoItem(id: String)

    @Query("SELECT * FROM TodoEntity WHERE id = :id")
    suspend fun getTodoById(id: String): TodoEntity?
}
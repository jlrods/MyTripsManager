package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTypeDao {
    @Query("SELECT * FROM expense_types ORDER BY name ASC")
    fun getAllExpenseTypes(): Flow<List<ExpenseType>>

    @Query("SELECT * FROM expense_types WHERE id = :id")
    fun getExpenseType(id: Int): Flow<ExpenseType>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expenseType: ExpenseType)

    @Update
    suspend fun update(expenseType: ExpenseType)

    @Delete
    suspend fun delete(expenseType: ExpenseType)
}
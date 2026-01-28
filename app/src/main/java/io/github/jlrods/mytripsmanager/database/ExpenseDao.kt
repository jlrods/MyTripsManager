package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpense(id: Int): Flow<Expense>

    @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY date DESC")
    fun getExpensesByTrip(tripId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE typeId = :typeId ORDER BY date DESC")
    fun getExpensesByType(typeId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE providerId = :providerId ORDER BY date DESC")
    fun getExpensesByProvider(providerId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND typeId = :typeId ORDER BY date DESC")
    fun getExpensesByTripAndType(tripId: Int, typeId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByTripAndDateRange(tripId: Int, startDate: Long, endDate: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND cost > :cost ORDER BY cost DESC")
    fun getExpensesByTripAndCostGreaterThan(tripId: Int, cost: Double): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}
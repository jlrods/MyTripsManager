package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.Expense
import io.github.jlrods.mytripsmanager.database.ExpenseDao
import io.github.jlrods.mytripsmanager.database.ExpenseType
import io.github.jlrods.mytripsmanager.database.ExpenseTypeDao
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val expenseTypeDao: ExpenseTypeDao
) {

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    fun getExpensesByTrip(
        tripId: Int
    ): Flow<List<Expense>> {

        return expenseDao.getExpensesByTrip(tripId)

    }
    fun getExpensesForTrip(tripId: Int) =
        expenseDao.getExpensesByTrip(tripId)

    fun getAllExpenseTypes(): Flow<List<ExpenseType>> {
        return expenseTypeDao.getAllExpenseTypes()
    }
}
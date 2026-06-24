package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.Expense
import io.github.jlrods.mytripsmanager.database.ExpenseDao
import io.github.jlrods.mytripsmanager.database.ExpenseType
import io.github.jlrods.mytripsmanager.database.ExpenseTypeDao
import io.github.jlrods.mytripsmanager.database.TripDao
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val expenseTypeDao: ExpenseTypeDao,
    private val tripDao: TripDao
) {

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
        updateTotaCost(expense.tripId)
        if (expense.isCash) {
            updateCashSpent(expense.tripId)
        }
    }

    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
        updateTotaCost(expense.tripId)
        if (expense.isCash) {
            updateCashSpent(expense.tripId)
        }
    }

    suspend fun update(expense: Expense){
        expenseDao.update(expense)
        updateTotaCost(expense.tripId)
        if (expense.isCash) {
            updateCashSpent(expense.tripId)
        }
    }

    private suspend fun updateTotaCost(tripId: Int) {
        val total = expenseDao.getTotalCostForTrip(tripId)
        tripDao.updateTotalCost(tripId, totalCost = total)
    }

    private suspend fun updateCashSpent(tripId: Int) {
        val cashSpent = expenseDao.getCashSpentForTrip(tripId)
        tripDao.updateCashSpent(tripId, cashSpent = cashSpent)
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
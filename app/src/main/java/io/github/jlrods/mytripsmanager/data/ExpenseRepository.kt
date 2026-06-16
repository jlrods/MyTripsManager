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


        val total =
            expenseDao.getTotalCostForTrip(
                expense.tripId
            )

        tripDao.updateTotalCost(
            expense.tripId,
            total
        )
    }

    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)

        val total =
            expenseDao.getTotalCostForTrip(
                expense.tripId
            )
        tripDao.updateTotalCost(
            expense.tripId,
            total
        )
    }

    suspend fun update(
        expense: Expense
    ){
        expenseDao.update(expense)
    }
    fun updateTripTotalCost(
        tripId: Int
    ) {
        val expenses = expenseDao.getExpensesByTrip(tripId)
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
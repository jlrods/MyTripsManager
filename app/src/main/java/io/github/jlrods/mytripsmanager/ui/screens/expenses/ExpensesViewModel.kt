package io.github.jlrods.mytripsmanager.ui.screens.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.ExpenseRepository
import io.github.jlrods.mytripsmanager.database.Expense
import io.github.jlrods.mytripsmanager.database.ExpenseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpensesViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    fun insertExpense(
        expense: Expense,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.insert(expense)
            onSuccess()
        }
    }

    fun getExpensesForTrip(
        tripId: Int
    ) = repository.getExpensesForTrip(tripId)

    fun getAllExpenseTypes(): Flow<List<ExpenseType>> {
        return repository.getAllExpenseTypes()
    }

    fun deleteExpense(expense: Expense) {
        Log.d(
            "DELETE_EXPENSE",
            "Deleting ${expense.id} ${expense.name}"
        )
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    fun updateExpense(
        expense: Expense,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {
            repository.update(expense)
            onSuccess()
        }
    }
}
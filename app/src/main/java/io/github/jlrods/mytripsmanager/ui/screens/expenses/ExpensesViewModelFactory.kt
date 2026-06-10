import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jlrods.mytripsmanager.data.ExpenseRepository
import io.github.jlrods.mytripsmanager.ui.screens.expenses.ExpensesViewModel

class ExpensesViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
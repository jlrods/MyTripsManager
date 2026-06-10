package io.github.jlrods.mytripsmanager.ui.screens.expenses

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.*

@Composable
fun ExpenseFormScreen(
    trip: Trip,
    viewModel: ExpensesViewModel,
    providers: List<Provider>,
    expenseTypes: List<ExpenseType>,
    modifier: Modifier = Modifier,
    onSave: () -> Unit
) {

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }

    var selectedProvider by rememberSaveable { mutableStateOf<Provider?>(null) }
    var selectedType by rememberSaveable { mutableStateOf<ExpenseType?>(null) }

    var showProviderDialog by rememberSaveable { mutableStateOf(false) }
    var showTypeDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Expense Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cost,
            onValueChange = { cost = it },
            label = { Text("Cost") },
            modifier = Modifier.fillMaxWidth()
        )

        // EXPENSE TYPE
        OutlinedTextField(
            value = selectedType?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Expense Type") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showTypeDialog = true }) {
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            }
        )

        // PROVIDER
        OutlinedTextField(
            value = selectedProvider?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Provider") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showProviderDialog = true }) {
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            }
        )

        Button(
            onClick = {

                val parsedCost = cost.toDoubleOrNull()

                if (
                    name.isBlank() ||
                    parsedCost == null ||
                    selectedType == null ||
                    selectedProvider == null
                ) {
                    Toast.makeText(
                        context,
                        "Please complete all fields correctly",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                viewModel.insertExpense(
                    name = name,
                    tripId = trip.id,
                    typeId = selectedType!!.id,
                    providerId = selectedProvider!!.id,
                    date = System.currentTimeMillis(),
                    cost = parsedCost
                ) {
                    onSave()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Expense")
        }
    }

    // ---------------- PROVIDER DIALOG ----------------
    if (showProviderDialog) {

        AlertDialog(
            onDismissRequest = { showProviderDialog = false },
            title = { Text("Select Provider") },
            confirmButton = {},
            text = {
                LazyColumn {
                    items(providers) { provider ->
                        Text(
                            text = provider.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedProvider = provider
                                    showProviderDialog = false
                                }
                                .padding(12.dp)
                        )
                    }
                }
            }
        )
    }

    // ---------------- TYPE DIALOG ----------------
    if (showTypeDialog) {

        AlertDialog(
            onDismissRequest = { showTypeDialog = false },
            title = { Text("Select Expense Type") },
            confirmButton = {},
            text = {
                LazyColumn {
                    items(expenseTypes) { type ->
                        Text(
                            text = type.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedType = type
                                    showTypeDialog = false
                                }
                                .padding(12.dp)
                        )
                    }
                }
            }
        )
    }
}
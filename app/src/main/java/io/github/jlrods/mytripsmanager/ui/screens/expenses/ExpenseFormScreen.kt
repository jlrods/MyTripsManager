package io.github.jlrods.mytripsmanager.ui.screens.expenses

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Delete
import io.github.jlrods.mytripsmanager.database.*
import io.github.jlrods.mytripsmanager.ui.components.ProviderLogo
import io.github.jlrods.mytripsmanager.ui.components.SelectableIconField

@Composable
fun ExpenseFormScreen(
    trip: Trip,
    viewModel: ExpensesViewModel,
    providers: List<Provider>,
    expenseTypes: List<ExpenseType>,
    modifier: Modifier = Modifier,
    expenseToEdit: Expense? = null,
    onSave: () -> Unit
) {

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }

    var selectedProvider by rememberSaveable { mutableStateOf<Provider?>(null) }
    var selectedType by rememberSaveable { mutableStateOf<ExpenseType?>(null) }

    var showProviderDialog by rememberSaveable { mutableStateOf(false) }
    var showTypeDialog by rememberSaveable { mutableStateOf(false) }

    var isCash by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(expenseToEdit, providers, expenseTypes) {

        expenseToEdit?.let { expense ->


            name = expense.name

            cost = expense.cost.toString()

//            selectedDate = expense.date

            selectedProvider =
                providers.firstOrNull {
                    it.id == expense.providerId
                }

            selectedType =
                expenseTypes.firstOrNull {
                    it.id == expense.typeId
                }
            isCash = expense.isCash
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            if(expenseToEdit == null)
                "Add Expense"
            else
                "Edit Expense",
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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Paid with cash?")

            Spacer(Modifier.width(12.dp))

            Switch(
                checked = isCash,
                onCheckedChange = { isCash = it }
            )
        }

        // EXPENSE TYPE
        Column(modifier = Modifier.fillMaxWidth()) {

            SelectableIconField(

                label = "Expense Type",

                text = selectedType?.name
                    ?: "Select expense type",

                iconRes = selectedType?.iconRes,

                logoUri = selectedProvider?.logoUri,

                onClick = {
                    showTypeDialog = true
                }

            )
            Divider()
        }

        // PROVIDER
        Column(modifier = Modifier.fillMaxWidth()) {

            SelectableIconField(

                label = "Provider",

                text = selectedProvider?.name
                    ?: "Select provider",

                iconRes = selectedProvider?.logoRes,

                logoUri = selectedProvider?.logoUri,

                onClick = {
                    showProviderDialog = true
                }

            )
            Divider()
        }

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

                if(expenseToEdit == null) {

                    viewModel.insertExpense(
                        Expense(
                            name = name,

                            tripId = trip.id,

                            typeId = selectedType!!.id,

                            providerId = selectedProvider!!.id,

                            date = System.currentTimeMillis(),

                            cost = cost.toDouble(),

                            isCash = isCash
                        )

                    ) {
                        onSave()
                    }

                }
                else {

                    viewModel.updateExpense(

                        expenseToEdit.copy(

                            name = name,

                            typeId = selectedType!!.id,

                            providerId = selectedProvider!!.id,

//                            date = selectedDate,

                            cost = cost.toDouble(),

                            isCash = isCash
                        )
                    ) {
                        onSave()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if(expenseToEdit == null)
                    "Save Expense"
                else
                    "Update Expense"
            )
        }
    }
    
    if (showProviderDialog) {
        ProviderPickerDialog(
            providers = providers,
            onProviderSelected = {
                selectedProvider = it
                showProviderDialog = false
            },
            onDismiss = { showProviderDialog = false }
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedType = type
                                    showTypeDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = type.iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(
                                    MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(Modifier.width(12.dp))

                            Text(type.name)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ProviderPickerDialog(
    providers: List<Provider>,
    onProviderSelected: (Provider) -> Unit,
    onDismiss: () -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    val filteredProviders = providers
        .filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
        .sortedBy { it.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Select Provider") },
        text = {

            Column {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search provider") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(filteredProviders) { provider ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onProviderSelected(provider)
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            ProviderLogo(
                                logoRes = provider.logoRes,
                                logoUri = provider.logoUri,
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = provider.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    )
}
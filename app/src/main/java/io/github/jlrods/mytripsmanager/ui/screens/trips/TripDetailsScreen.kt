package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.ExpenseType
import io.github.jlrods.mytripsmanager.database.Provider
import io.github.jlrods.mytripsmanager.database.Trip
import io.github.jlrods.mytripsmanager.ui.components.ProviderLogo
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel
import io.github.jlrods.mytripsmanager.ui.screens.expenses.ExpensesViewModel

enum class SelectionMode {
    NONE,
    DESTINATION,
    EXPENSE
}
@Composable
fun TripDetailScreen(
    viewModel: TripsViewModel,
    citiesViewModel: CitiesViewModel,
    expensesViewModel: ExpensesViewModel,
    providers: List<Provider>,
    expenseTypes: List<ExpenseType>,
    modifier: Modifier = Modifier,
    tripToEdit: Trip? = null,
    onAddDestinationClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onSave: () -> Unit
) {
    val destinations by viewModel.tripDestinations.collectAsState()
    LaunchedEffect(tripToEdit?.id) {

        tripToEdit?.id?.let {

            viewModel.loadDestinationsForTrip(it)
        }
    }
    val expenses by expensesViewModel.getExpensesForTrip(tripToEdit?.id ?: 0)
        .collectAsState(initial = emptyList())

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var selectionMode by remember {
        mutableStateOf(SelectionMode.NONE)
    }

    var selectedDestinations by remember {
        mutableStateOf(setOf<Int>())
    }

    var selectedExpenses by remember {
        mutableStateOf(setOf<Int>())
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
            tripToEdit?.let { trip ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = trip.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Trip"
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    TripInfoRow(
                        label = "Start Date",
                        value = formatDate(trip.start)
                    )

                    TripInfoRow(
                        label = "End Date",
                        value = formatDate(trip.end)
                    )

                    TripInfoRow(
                        label = "Cash Budget",
                        value = "€%.2f".format(trip.cashBudget)
                    )

                    TripInfoRow(
                        label = "Cash Spent",
                        value = "€%.2f".format(trip.cashSpent)
                    )

                    TripInfoRow(
                        label = "Total Cost",
                        value = "€%.2f".format(trip.totalCost)
                    )

                    Spacer(Modifier.height(32.dp))

                    Text(
                        text = "Destinations",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    if (destinations.isEmpty()) {

                        Text("No destinations yet")

                    } else {

                        destinations.forEach { destination ->
                            val isSelected =
                                selectedDestinations.contains(
                                    destination.destination.id
                                )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            Color.Transparent
                                    )
                                    .combinedClickable(
                                        onClick = {

                                            if (selectionMode == SelectionMode.DESTINATION) {

                                                selectedDestinations =
                                                    if (selectedDestinations.contains(destination.destination.id)) {

                                                        selectedDestinations -
                                                                destination.destination.id

                                                    } else {

                                                        selectedDestinations +
                                                                destination.destination.id
                                                    }
                                            }
                                        },


                                        onLongClick = {

                                            selectionMode = SelectionMode.DESTINATION

                                            selectedExpenses = emptySet()

                                            selectedDestinations =
                                                setOf(destination.destination.id)
                                        }
                                    )
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Image(
                                        painter = painterResource(
                                            id = destination.cityWithCountry.country.flagRes
                                        ),
                                        contentDescription =
                                            destination.cityWithCountry.country.name,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                    )

                                    Spacer(Modifier.width(12.dp))

                                    Text(
                                        text =
                                            destination.cityWithCountry.city.name
                                                .replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text =
                                        "${formatDate(destination.destination.start)} → ${
                                            formatDate(destination.destination.end)
                                        }",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 40.dp)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Expenses",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))


                    if (expenses.isEmpty()) {

                        Text("No expenses yet")

                    } else {


                        expenses.forEach { expense ->

                            val provider =
                                providers.firstOrNull {
                                    it.id == expense.providerId
                                }

                            val type =
                                expenseTypes.firstOrNull {
                                    it.id == expense.typeId
                                }
                            val isSelected =
                                selectedExpenses.contains(
                                    expense.id
                                )
                            Column(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            Color.Transparent
                                    )
                                    .combinedClickable(
                                        onClick = {

                                            if (selectionMode == SelectionMode.EXPENSE) {

                                                selectedExpenses =
                                                    if (selectedExpenses.contains(expense.id)) {

                                                        selectedExpenses - expense.id

                                                    } else {

                                                        selectedExpenses + expense.id
                                                    }
                                            }
                                        },


                                        onLongClick = {

                                            selectionMode = SelectionMode.EXPENSE

                                            selectedDestinations = emptySet()

                                            selectedExpenses =
                                                setOf(expense.id)
                                        }
                                    )


                                    ) {


                                Row(

                                    verticalAlignment = Alignment.CenterVertically

                                ) {


                                    // Expense Type icon

                                    type?.let {


                                        Image(

                                            painter = painterResource(id = it.iconRes),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp),
                                            colorFilter = ColorFilter.tint(
                                                    MaterialTheme.colorScheme.onSurface
                                                    )
                                        )

                                    }



                                    Spacer(
                                        Modifier.width(12.dp)
                                    )
                                    Column {


                                        Text(

                                            text = expense.name,

                                            style = MaterialTheme.typography.bodyLarge

                                        )

                                        Row(

                                            verticalAlignment = Alignment.CenterVertically

                                        ) {


                                            provider?.let {


                                                ProviderLogo(

                                                    logoRes = it.logoRes,

                                                    modifier = Modifier.size(20.dp)

                                                )


                                                Spacer(
                                                    Modifier.width(6.dp)
                                                )

                                            }


                                            Text(

                                                text =
                                                    provider?.name
                                                        ?: "Unknown provider",

                                                style =
                                                    MaterialTheme.typography.bodyMedium

                                            )

                                        }


                                    }

                                    Spacer(
                                        Modifier.weight(1f)
                                    )



                                    Text(

                                        text =
                                            "€%.2f".format(expense.cost),

                                        style =
                                            MaterialTheme.typography.bodyLarge

                                    )


                                }

                            }

                        }

                    }

                    Spacer(Modifier.height(96.dp))
                }

            } ?: Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading trip...")
            }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {

            FloatingActionButton(
                onClick = onAddDestinationClick
            ) {
                Text("D")
            }

            Spacer(Modifier.height(8.dp))

            FloatingActionButton(
                onClick = onAddExpenseClick
            ) {
                Text("E")
            }
        }

        if (showDeleteDialog) {

            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text("Delete Trip")
                },
                text = {
                    Text("Are you sure you want to delete this trip?")
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            tripToEdit?.let {

                                viewModel.deleteTrip(it)
                            }

                            showDeleteDialog = false

                            onSave()
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }


        if (selectionMode != SelectionMode.NONE) {

            Column(
                horizontalAlignment = Alignment.End
            ) {


                FloatingActionButton(

                    onClick = {

                        selectionMode = SelectionMode.NONE

                        selectedDestinations = emptySet()

                        selectedExpenses = emptySet()
                    }

                ) {

                    Text("X")
                }


                Spacer(
                    Modifier.height(8.dp)
                )


                FloatingActionButton(

                    onClick = {

                        when(selectionMode) {

                            SelectionMode.DESTINATION -> {

                                selectedDestinations.forEach { id ->

                                    destinations
                                        .firstOrNull {
                                            it.destination.id == id
                                        }
                                        ?.let {

                                            viewModel.deleteDestination(
                                                it.destination
                                            )
                                        }
                                }
                            }


                            SelectionMode.EXPENSE -> {

                                selectedExpenses.forEach { id ->

                                    expenses
                                        .firstOrNull {
                                            it.id == id
                                        }
                                        ?.let { expense ->

                                            expensesViewModel.deleteExpense(
                                                expense
                                            )
                                        }
                                }
                            }


                            else -> {}
                        }


                        selectionMode = SelectionMode.NONE

                        selectedDestinations = emptySet()

                        selectedExpenses = emptySet()

                    }

                ) {

                    Text("🗑")
                }
            }
        }
    }

    }

    @Composable
    fun TripInfoRow(
        label: String,
        value: String
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
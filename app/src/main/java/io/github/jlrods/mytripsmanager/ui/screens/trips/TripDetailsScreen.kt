package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.Trip
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel

@Composable
fun TripDetailScreen(
    viewModel: TripsViewModel,
    citiesViewModel: CitiesViewModel,
    modifier: Modifier = Modifier,
    tripToEdit: Trip? = null,
    onSave: () -> Unit
) {

    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { /* Add Destination */ }
                ) {
                    Text("D")
                }

                Spacer(Modifier.height(8.dp))

                FloatingActionButton(
                    onClick = { /* Add Expense */ }
                ) {
                    Text("E")
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = tripToEdit?.name ?: "",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(24.dp))

            TripInfoRow(
                label = "Start Date",
                value = formatDate(tripToEdit?.start ?: 0L)
            )

            TripInfoRow(
                label = "End Date",
                value = formatDate(tripToEdit?.end ?: 0L)
            )

            TripInfoRow(
                label = "Cash Budget",
                value = "€%.2f".format(tripToEdit?.cashBudget ?: 0.0)
            )

            TripInfoRow(
                label = "Cash Spent",
                value = "€%.2f".format(tripToEdit?.cashSpent ?: 0.0)
            )

            TripInfoRow(
                label = "Total Cost",
                value = "€%.2f".format(tripToEdit?.totalCost ?: 0.0)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Destinations",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text("No destinations yet")

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Expenses",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text("No expenses yet");
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
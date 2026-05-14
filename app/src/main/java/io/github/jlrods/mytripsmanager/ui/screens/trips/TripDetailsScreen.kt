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

@Composable
fun TripDetailScreen(
    trip: Trip?,
    onBack: () -> Unit
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
                text = trip?.name ?: "",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            Text("Destinations")
            // list later

            Spacer(Modifier.height(16.dp))

            Text("Expenses")
            // list later
        }
    }
}
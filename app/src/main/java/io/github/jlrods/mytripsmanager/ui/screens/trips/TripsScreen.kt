//

package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.Trip

@Composable
fun TripsScreen(
    modifier: Modifier = Modifier,
    viewModel: TripsViewModel,
    onAddTripClick: () -> Unit
) {

    val trips by viewModel.trips.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTripClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Trip"
                )
            }
        }
    ) { innerPadding ->

        if (trips.isEmpty()) {

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No trips yet")
            }

        } else {

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(trips) { trip ->

                    TripItem(trip = trip)

                }
            }
        }
    }
}

@Composable
fun TripItem(
    trip: Trip
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = trip.name
            )

            Text(
                text = "Budget: ${trip.cashBudget}"
            )

            Text(
                text = "Spent: ${trip.cashSpent}"
            )
        }
    }
}
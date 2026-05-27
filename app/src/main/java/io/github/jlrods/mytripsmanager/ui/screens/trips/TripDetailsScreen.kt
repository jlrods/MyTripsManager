package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
    val destinations by viewModel.tripDestinations.collectAsState()
    LaunchedEffect(tripToEdit?.id) {

        tripToEdit?.id?.let {

            viewModel.loadDestinationsForTrip(it)
        }
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
                        .padding(16.dp)
                ) {

                    Text(
                        text = trip.name,
                        style = MaterialTheme.typography.titleLarge
                    )

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

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {

                                Image(
                                    painter = painterResource(
                                        id = destination.cityWithCountry.country.flagRes
                                    ),
                                    contentDescription = destination.cityWithCountry.country.name,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                )

                                Spacer(Modifier.width(12.dp))

                                Text(
                                    text =
                                        destination.cityWithCountry.city.name
                                            .replaceFirstChar { it.uppercase() }
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

                    Text("No expenses yet")
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
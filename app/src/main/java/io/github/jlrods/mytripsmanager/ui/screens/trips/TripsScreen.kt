package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.TripListItem

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
                    TripItem(trip)
                }
            }
        }
    }
}


@Composable
fun TripItem(
    trip: TripListItem
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT SIDE CONTENT
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = trip.name.replaceFirstChar {
                        it.uppercase()
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Column {

                    Row {
                        Text(
                            text = "Start:",
                            modifier = Modifier.width(70.dp)
                        )

                        Text(
                            text = formatDate(trip.startDate)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Row {
                        Text(
                            text = "End:",
                            modifier = Modifier.width(70.dp)
                        )

                        Text(
                            text = formatDate(trip.endDate)
                        )
                    }
                }
            }

            // RIGHT SIDE FLAG
            if (trip.flagRes != null) {

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Image(
                    painter = painterResource(id = trip.flagRes),
                    contentDescription = trip.countryName,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
        }
    }
}
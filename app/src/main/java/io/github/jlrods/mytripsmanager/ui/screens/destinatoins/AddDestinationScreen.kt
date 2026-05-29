package io.github.jlrods.mytripsmanager.ui.screens.destinations

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.CityWithCountry
import io.github.jlrods.mytripsmanager.database.Trip
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel
import io.github.jlrods.mytripsmanager.ui.screens.trips.TripsViewModel
import io.github.jlrods.mytripsmanager.ui.screens.trips.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDestinationScreen(
    trip: Trip,
    viewModel: TripsViewModel,
    citiesViewModel: CitiesViewModel,
    modifier: Modifier = Modifier,
    onSave: () -> Unit
) {

    val context = LocalContext.current

    val cities by citiesViewModel.cities.collectAsState()

    var selectedCity by rememberSaveable {
        mutableStateOf<CityWithCountry?>(null)
    }

    var showCityDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var startDate by rememberSaveable {
        mutableStateOf(System.currentTimeMillis())
    }

    var endDate by rememberSaveable {
        mutableStateOf(System.currentTimeMillis())
    }

    var showStartDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var showEndDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Add Destination",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value =
                selectedCity?.city?.name
                    ?.replaceFirstChar { it.uppercase() }
                    ?: "",
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Destination City")
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                IconButton(
                    onClick = {
                        showCityDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showStartDatePicker = true
                }
        ) {

            OutlinedTextField(
                value = formatDate(startDate),
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = {
                    Text("Start Date")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showEndDatePicker = true
                }
        ) {

            OutlinedTextField(
                value = formatDate(endDate),
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = {
                    Text("End Date")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {

                val cityId = selectedCity?.city?.id

                if (cityId == null) {

                    Toast.makeText(
                        context,
                        "Please select a city",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                if (endDate < startDate) {

                    Toast.makeText(
                        context,
                        "End date cannot be before start date",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                if (startDate < trip.start){
                    Toast.makeText(
                        context,
                        "Start date cannot be before trip start date",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (endDate > trip.end){
                    Toast.makeText(
                        context,
                        "End date cannot be after trip end date",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                viewModel.insertDestination(
                    tripId = trip.id,
                    cityId = cityId,
                    start =  startDate,
                    end = endDate,
                    onSuccess = {
                        onSave()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        {

            Text("Save Destination")
        }
    }

    if (showCityDialog) {

        AlertDialog(
            onDismissRequest = {
                showCityDialog = false
            },
            confirmButton = {},
            title = {
                Text("Select Destination City")
            },
            text = {

                LazyColumn {

                    items(cities) { city ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    selectedCity = city
                                    showCityDialog = false
                                }
                                .padding(
                                    vertical = 10.dp,
                                    horizontal = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(
                                    id = city.country.flagRes
                                ),
                                contentDescription = city.country.name,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Spacer(
                                modifier = Modifier.width(12.dp)
                            )

                            Text(
                                text =
                                    "${city.city.name.replaceFirstChar { it.uppercase() }} - ${
                                        city.country.name.replaceFirstChar { it.uppercase() }
                                    }",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        )
    }

    if (showStartDatePicker) {

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDate
        )

        DatePickerDialog(
            onDismissRequest = {
                showStartDatePicker = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        startDate =
                            datePickerState.selectedDateMillis
                                ?: startDate

                        showStartDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showStartDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }

    if (showEndDatePicker) {

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate
        )

        DatePickerDialog(
            onDismissRequest = {
                showEndDatePicker = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        endDate =
                            datePickerState.selectedDateMillis
                                ?: endDate

                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showEndDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }
}
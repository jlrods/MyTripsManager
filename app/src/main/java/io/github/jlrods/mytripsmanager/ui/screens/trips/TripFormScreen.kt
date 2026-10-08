package io.github.jlrods.mytripsmanager.ui.screens.trips

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import io.github.jlrods.mytripsmanager.database.CityWithCountry
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import io.github.jlrods.mytripsmanager.ui.components.SearchablePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripFormScreen(
    viewModel: TripsViewModel,
    citiesViewModel: CitiesViewModel,
    modifier: Modifier = Modifier,
    onSave: () -> Unit
) {

    val context = LocalContext.current

    var tripName by rememberSaveable {
        mutableStateOf("")
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

    val cities by citiesViewModel.cities.collectAsState()

    var selectedCity by rememberSaveable {
        mutableStateOf<CityWithCountry?>(null)
    }

    var showCityDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = tripName,
            onValueChange = {
                tripName = it
            },
            label = {
                Text("Trip Name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (tripName.isBlank()) {

                    Toast.makeText(
                        context,
                        "Trip name cannot be empty",
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

                viewModel.insertTripWithDestination(
                    name = tripName,
                    startDate = startDate,
                    endDate = endDate,
                    cityId = selectedCity?.city?.id,
                    onDuplicate = {

                        Toast.makeText(
                            context,
                            "Trip already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onSuccess = {
                        onSave()
                    },
                    onMissingCity = {

                        Toast.makeText(
                            context,
                            "Please select a destination city",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Save Trip")
        }
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

    if (showCityDialog) {
        DestinationCityPickerDialog(
            cities = cities,
            onCitySelected = {
                selectedCity = it
                showCityDialog = false
            },
            onDismiss = {
                showCityDialog = false
            }
        )
    }
}

fun formatDate(timestamp: Long): String {

    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(
        Date(timestamp)
    )
}

@Composable
fun DestinationCityPickerDialog(
    cities: List<CityWithCountry>,
    onCitySelected: (CityWithCountry) -> Unit,
    onDismiss: () -> Unit
) {

    SearchablePickerDialog(

        title = "Select Destination City",

        items = cities.sortedWith(
            compareBy(
                { it.country.name },
                { it.city.name }
            )
        ),

        searchText = {
            "${it.city.name} ${it.country.name}"
        },

        onItemSelected = onCitySelected,

        onDismiss = onDismiss

    ) { city ->

        Image(
            painter = painterResource(city.country.flagRes),
            contentDescription = city.country.name,
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(12.dp))

        Text(
            "${city.city.name.replaceFirstChar { it.uppercase() }} - ${
                city.country.name.replaceFirstChar { it.uppercase() }
            }",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
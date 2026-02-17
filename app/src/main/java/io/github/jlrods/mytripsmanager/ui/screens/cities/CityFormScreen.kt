package io.github.jlrods.mytripsmanager.ui.screens.cities

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.Country
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import io.github.jlrods.mytripsmanager.database.CityWithCountry


@Composable
fun CityFormScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel,
    cityToEdit: CityWithCountry? = null,
    onSave: () -> Unit
) {

    val isEditMode = cityToEdit != null

    var cityName by rememberSaveable(cityToEdit) {
        mutableStateOf(cityToEdit?.city?.name?.trim()?.replaceFirstChar { it.uppercase() } ?: "")
    }

    var selectedCountryId by rememberSaveable(cityToEdit) {
        mutableStateOf(cityToEdit?.country?.id)
    }
    var showCountryDialog by remember { mutableStateOf(false) }
    val countries by viewModel.countries.collectAsState(initial = emptyList())
    var selectedCountry = countries.firstOrNull {
        it.id == selectedCountryId
    }
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("City Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedCountry?.name
                    ?.trim()
                    ?.replaceFirstChar { it.uppercase() }
                    ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Country") },
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showCountryDialog = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )

            if (showCountryDialog) {
                CountryPickerDialog(
                    countries = countries,
                    onCountrySelected = {
                        selectedCountry = it
                        showCountryDialog = false
                    },
                    onDismiss = { showCountryDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (cityName.isNotBlank() && selectedCountry != null) {

                        if (!isEditMode) {
                            // ADD MODE
                            viewModel.insertCity(
                                name = cityName,
                                countryId = selectedCountry.id,
                                onDuplicate = {
                                    Toast.makeText(
                                        context,
                                        "City already exists for this country",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onSuccess = { onSave() }
                            )
                        } else {
                            // EDIT MODE
                            cityToEdit?.let {
                                viewModel.updateCity(
                                    id = it.city.id,
                                    name = cityName,
                                    countryId = selectedCountry.id,
                                )
                            }
                            onSave()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Update City" else "Save City")
            }
            if (isEditMode) {
                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete City"
                    )
                }
            }
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete City") },
                text = { Text("Are you sure you want to delete this city?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            cityToEdit?.let {
                                viewModel.deleteCity(it.city)
                            }
                            showDeleteDialog = false
                            Toast.makeText(
                                context,
                                "City deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                            onSave()
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun CountryPickerDialog(
    countries: List<Country>,
    onCountrySelected: (Country) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = countries
        .filter { it.name.contains(searchQuery, ignoreCase = true) }
        .sortedBy { it.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Select Country") },
        text = {
            Column {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(filteredCountries) { country ->

                        Row(
                            modifier = Modifier
                                .heightIn(max = 400.dp)
                                .fillMaxWidth()
                                .clickable { onCountrySelected(country) }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Log.d("Jose", country.name)
                            Image(
                                painter = painterResource(id = country.flagRes),
                                contentDescription = country.name,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )


                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = country.name.trim().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    )
}


package io.github.jlrods.mytripsmanager.ui.screens.cities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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


@Composable
fun AddCityScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel,
    onSave: () -> Unit
) {

    var cityName by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }

    val countries by viewModel.countries.collectAsState(initial = emptyList())

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

            CountryDropdown(
                countries = countries,
                selectedCountry = selectedCountry,
                onCountrySelected = { selectedCountry = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (cityName.isNotBlank() && selectedCountry != null) {
                        viewModel.insertCity(
                            cityName,
                            selectedCountry!!.id
                        )
                        onSave()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save City")
            }
        }
    }
}

@Composable
fun CountryDropdown(
    countries: List<Country>,
    selectedCountry: Country?,
    onCountrySelected: (Country) -> Unit
) {
    // temporary placeholder so app compiles
    Text("Country selector coming soon")
}
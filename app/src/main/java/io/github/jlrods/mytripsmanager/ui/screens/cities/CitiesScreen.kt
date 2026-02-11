package io.github.jlrods.mytripsmanager.ui.screens.cities

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun CitiesScreen(
    viewModel: CitiesViewModel
) {
    val cities by viewModel.cities.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(cities) { city ->
            Text(text = city.name)
        }
    }
}
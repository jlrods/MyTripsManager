package io.github.jlrods.mytripsmanager.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.jlrods.mytripsmanager.MainScreen
import io.github.jlrods.mytripsmanager.ui.components.MyTripsManagerTopAppBar
import io.github.jlrods.mytripsmanager.ui.navigation.AppDestinations

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jlrods.mytripsmanager.database.MyTripsManagerDb
import io.github.jlrods.mytripsmanager.data.CityRepository
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModelFactory
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesScreen

@PreviewScreenSizes
@Composable
fun MyTripsManagerApp() {

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TRIPS) }

    val context = LocalContext.current
    val database = MyTripsManagerDb.getDatabase(context)//TODO: This will be moved to Aplicatoin class later on.
    val repository = CityRepository(database.cityDao())
    val factory = CitiesViewModelFactory(repository)
    val citiesViewModel: CitiesViewModel = viewModel(factory = factory)

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painter = painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MyTripsManagerTopAppBar() }
        ) { innerPadding ->

            when (currentDestination) {
                AppDestinations.TRIPS -> {
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }

                AppDestinations.CITIES -> {
                    CitiesScreen(
                        viewModel = citiesViewModel
                    )
                }

                AppDestinations.PROVIDERS -> {
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

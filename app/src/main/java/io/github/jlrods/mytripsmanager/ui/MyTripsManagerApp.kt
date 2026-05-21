package io.github.jlrods.mytripsmanager.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import io.github.jlrods.mytripsmanager.data.DestinationRepository
import io.github.jlrods.mytripsmanager.data.ProviderRepository
import io.github.jlrods.mytripsmanager.data.TripRepository
import io.github.jlrods.mytripsmanager.database.Provider
import io.github.jlrods.mytripsmanager.database.Trip
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModelFactory
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesScreen
import io.github.jlrods.mytripsmanager.ui.screens.cities.CityFormScreen
import io.github.jlrods.mytripsmanager.ui.screens.providers.ProviderFormScreen
import io.github.jlrods.mytripsmanager.ui.screens.providers.ProvidersScreen
import io.github.jlrods.mytripsmanager.ui.screens.providers.ProvidersViewModel
import io.github.jlrods.mytripsmanager.ui.screens.providers.ProvidersViewModelFactory
import io.github.jlrods.mytripsmanager.ui.screens.trips.TripDetailScreen
import io.github.jlrods.mytripsmanager.ui.screens.trips.TripFormScreen
import io.github.jlrods.mytripsmanager.ui.screens.trips.TripsScreen
import io.github.jlrods.mytripsmanager.ui.screens.trips.TripsViewModel
import io.github.jlrods.mytripsmanager.ui.screens.trips.TripsViewModelFactory

@PreviewScreenSizes
@Composable
fun MyTripsManagerApp() {

    //City screen initialization
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TRIPS) }
    var selectedCityId by rememberSaveable { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val database = MyTripsManagerDb.getDatabase(context)//TODO: This will be moved to Aplicatoin class later on.
    val repository = CityRepository(database.cityDao(), database.countryDao())
    val factory = CitiesViewModelFactory(repository)
    val citiesViewModel: CitiesViewModel = viewModel(factory = factory)

    //Provider screen initialization
    val providerRepository = ProviderRepository(database.providerDao())
    val providersFactory = ProvidersViewModelFactory(providerRepository)
    val providersViewModel: ProvidersViewModel =
        viewModel(factory = providersFactory)
    var selectedProvider by remember { mutableStateOf<Provider?>(null) }

    //Trips screen initialization
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }
    val tripRepository = TripRepository(database.tripDao())

    val destinationRepository = DestinationRepository(database.destinationDao())
    val tripFactory = TripsViewModelFactory(tripRepository,destinationRepository)
    val tripsViewModel: TripsViewModel = viewModel(factory = tripFactory)


    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries
                .filter { it.showInBottomNav }
                .forEach {
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
                    TripsScreen(
                        viewModel = tripsViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onAddTripClick = {
                            currentDestination = AppDestinations.ADD_TRIP
                        }

                    )
                }

                AppDestinations.ADD_TRIP -> {
                    TripFormScreen(
                        viewModel = tripsViewModel,
                        citiesViewModel = citiesViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onSave = {
                            currentDestination = AppDestinations.TRIPS
                        }
                    )
                }
                AppDestinations.TRIP_DETAILS -> {
                    TripDetailScreen(
                        trip = selectedTrip,
                        onBack = {
                            currentDestination = AppDestinations.TRIPS
                        }
                    )
                }



                AppDestinations.CITIES -> {
                    CitiesScreen(
                        viewModel = citiesViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onAddCityClick = {
                            currentDestination = AppDestinations.ADD_CITY
                        },
                        onCityClick = { city ->
                            selectedCityId = city.city.id
                            currentDestination = AppDestinations.EDIT_CITY
                        }
                    )
                }

                AppDestinations.ADD_CITY -> CityFormScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = citiesViewModel,
                    cityToEdit = null,
                    onSave = {
                        currentDestination = AppDestinations.CITIES
                    }
                )

                AppDestinations.EDIT_CITY -> {

                    val cities by citiesViewModel.cities.collectAsState()

                    val cityToEdit = cities.firstOrNull {
                        it.city.id == selectedCityId
                    }

                    CityFormScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = citiesViewModel,
                        cityToEdit = cityToEdit,
                        onSave = {
                            selectedCityId = null
                            currentDestination = AppDestinations.CITIES
                        }
                    )
                }

                AppDestinations.PROVIDERS -> {
                    ProvidersScreen(
                        viewModel = providersViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onAddProviderClick = {
                            selectedProvider = null
                            currentDestination = AppDestinations.ADD_PROVIDER
                        },
                        onProviderClick = { provider ->
                            selectedProvider = provider
                            currentDestination = AppDestinations.EDIT_PROVIDER
                        }
                    )
                }

                AppDestinations.ADD_PROVIDER -> {
                    ProviderFormScreen(
                        viewModel = providersViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onSave = {
                            currentDestination = AppDestinations.PROVIDERS
                        }
                    )
                }

                AppDestinations.EDIT_PROVIDER -> {
                    ProviderFormScreen(
                        viewModel = providersViewModel,
                        modifier = Modifier.padding(innerPadding),
                        providerToEdit = selectedProvider,
                        onSave = {
                            currentDestination = AppDestinations.PROVIDERS
                        }
                    )
                }

            }
        }
    }
}

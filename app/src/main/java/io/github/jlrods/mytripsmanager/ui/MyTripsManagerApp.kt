package io.github.jlrods.mytripsmanager.ui

import ExpensesViewModelFactory
import android.util.Log
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
import io.github.jlrods.mytripsmanager.ui.components.MyTripsManagerTopAppBar
import io.github.jlrods.mytripsmanager.ui.navigation.AppDestinations
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jlrods.mytripsmanager.database.MyTripsManagerDb
import io.github.jlrods.mytripsmanager.data.CityRepository
import io.github.jlrods.mytripsmanager.data.DestinationRepository
import io.github.jlrods.mytripsmanager.data.ExpenseRepository
import io.github.jlrods.mytripsmanager.data.ProviderRepository
import io.github.jlrods.mytripsmanager.data.TripRepository
import io.github.jlrods.mytripsmanager.database.Destination
import io.github.jlrods.mytripsmanager.database.Expense
import io.github.jlrods.mytripsmanager.database.Provider
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModel
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesViewModelFactory
import io.github.jlrods.mytripsmanager.ui.screens.cities.CitiesScreen
import io.github.jlrods.mytripsmanager.ui.screens.cities.CityFormScreen
import io.github.jlrods.mytripsmanager.ui.screens.destinations.AddDestinationScreen
import io.github.jlrods.mytripsmanager.ui.screens.expenses.ExpenseFormScreen
import io.github.jlrods.mytripsmanager.ui.screens.expenses.ExpensesViewModel
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
    var selectedTripId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    val tripRepository = TripRepository(database.tripDao(),database.destinationDao())

    val destinationRepository = DestinationRepository(database.destinationDao())
    val tripFactory = TripsViewModelFactory(tripRepository,destinationRepository)
    val tripsViewModel: TripsViewModel = viewModel(factory = tripFactory)

    val expenseRepository = ExpenseRepository(database.expenseDao(), database.expenseTypeDao(), database.tripDao())
    val expensesViewModelFactory = ExpensesViewModelFactory(expenseRepository)
    val expensesViewModel: ExpensesViewModel = viewModel(factory = expensesViewModelFactory)

    val tripToEdit by tripsViewModel.getTripById(selectedTripId ?: 0).collectAsState(initial = null)

    var selectedDestinationToEdit by rememberSaveable {mutableStateOf<Destination?>(null)}

    var selectedExpenseToEdit by rememberSaveable{mutableStateOf<Expense?>(null)}

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
        val appBarTitle = when (currentDestination) {

            AppDestinations.TRIPS -> "My Trips"

            AppDestinations.ADD_TRIP -> "Add Trip"
            AppDestinations.EDIT_TRIP -> tripToEdit?.name ?: "Trip Details"

            AppDestinations.ADD_DESTINATION -> "Add Destination"
            AppDestinations.EDIT_DESTINATION -> "Edit Destination"

            AppDestinations.ADD_EXPENSE -> "Add Expense"
            AppDestinations.EDIT_EXPENSE -> "Edit Expense"

            AppDestinations.CITIES -> "Cities"
            AppDestinations.ADD_CITY -> "Add City"
            AppDestinations.EDIT_CITY -> "Edit City"

            AppDestinations.PROVIDERS -> "Providers"
            AppDestinations.ADD_PROVIDER -> "Add Provider"
            AppDestinations.EDIT_PROVIDER -> "Edit Provider"
        }
        val showBackButton = (currentDestination != AppDestinations.TRIPS && currentDestination != AppDestinations.CITIES
                &&  currentDestination != AppDestinations.PROVIDERS)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
//            topBar = { MyTripsManagerTopAppBar() }
            topBar = {
                MyTripsManagerTopAppBar(
                    title = appBarTitle,
                    showBackButton = showBackButton,
                    onBack = {

                        when (currentDestination) {

                            AppDestinations.ADD_TRIP ->
                                currentDestination = AppDestinations.TRIPS

                            AppDestinations.EDIT_TRIP -> {
                                selectedTripId = null
                                currentDestination = AppDestinations.TRIPS
                            }

                            AppDestinations.ADD_DESTINATION,
                            AppDestinations.EDIT_DESTINATION -> {
                                selectedDestinationToEdit = null
                                currentDestination = AppDestinations.EDIT_TRIP
                            }

                            AppDestinations.ADD_EXPENSE,
                            AppDestinations.EDIT_EXPENSE -> {
                                selectedExpenseToEdit = null
                                currentDestination = AppDestinations.EDIT_TRIP
                            }

                            AppDestinations.ADD_CITY,
                            AppDestinations.EDIT_CITY -> {
                                selectedCityId = null
                                currentDestination = AppDestinations.CITIES
                            }

                            AppDestinations.ADD_PROVIDER,
                            AppDestinations.EDIT_PROVIDER -> {
                                selectedProvider = null
                                currentDestination = AppDestinations.PROVIDERS
                            }

                            else -> {}
                        }
                    }
                )
            }
        ) { innerPadding ->

            when (currentDestination) {
                AppDestinations.TRIPS -> {
                    TripsScreen(
                        viewModel = tripsViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onTripClick = { tripID ->
                            selectedTripId = tripID
                            currentDestination = AppDestinations.EDIT_TRIP
                        },
                        onAddTripClick = {
                            selectedTripId = null
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
                AppDestinations.EDIT_TRIP -> {
                    val providers by providersViewModel
                        .getAllProviders()
                        .collectAsState(initial = emptyList())
                    val expenseTypes by expensesViewModel
                        .getAllExpenseTypes()
                        .collectAsState(initial = emptyList())

                    TripDetailScreen(
                        viewModel = tripsViewModel,
                        citiesViewModel = citiesViewModel,
                        expensesViewModel = expensesViewModel,
                        providers = providers,
                        expenseTypes = expenseTypes,
                        modifier = Modifier.padding(innerPadding),
                        tripToEdit = tripToEdit,
                        onAddDestinationClick = {
                            currentDestination = AppDestinations.ADD_DESTINATION
                        },
                        onAddExpenseClick = {
                            currentDestination = AppDestinations.ADD_EXPENSE
                        },
                        onEditDestinationClick = { destination ->
                            Log.d(
                                "EDIT_DESTINATION_TEST",
                                "Clicked: ${destination.id} ${destination.cityId}"
                            )
                            selectedDestinationToEdit = destination

                            currentDestination =
                                AppDestinations.EDIT_DESTINATION
                        },
                        onEditExpenseClick = { expense ->

                            selectedExpenseToEdit = expense

                            currentDestination =
                                AppDestinations.EDIT_EXPENSE
                        },
                        onSave = {
                                selectedTripId = null
                                currentDestination = AppDestinations.TRIPS
                            },

                    )
                }

                AppDestinations.ADD_DESTINATION -> {
                    tripToEdit?.let { trip ->

                        AddDestinationScreen(
                            trip = trip,
                            viewModel = tripsViewModel,
                            citiesViewModel = citiesViewModel,
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding),
                            destinationToEdit = null,
                            onSave = {
                                currentDestination = AppDestinations.EDIT_TRIP
                            }
                        )
                    }
                }

                AppDestinations.EDIT_DESTINATION -> {
                    tripToEdit?.let { trip ->

                        AddDestinationScreen(
                            trip = trip,
                            viewModel = tripsViewModel,
                            citiesViewModel = citiesViewModel,
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding),
                            destinationToEdit = selectedDestinationToEdit,
                            onSave = {
                                selectedDestinationToEdit = null
                                currentDestination =
                                    AppDestinations.EDIT_TRIP
                            }
                        )
                    }
                }

                AppDestinations.ADD_EXPENSE -> {
                    val providers by providersViewModel
                        .getAllProviders()
                        .collectAsState(initial = emptyList())

                    val expenseTypes by expensesViewModel
                        .getAllExpenseTypes()
                        .collectAsState(initial = emptyList())

                    tripToEdit?.let { trip ->

                        ExpenseFormScreen(
                            trip = trip,
                            viewModel = expensesViewModel,
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding),
                            providers = providers,
                            expenseTypes = expenseTypes,
                            onSave = {
                                currentDestination = AppDestinations.EDIT_TRIP
                            }
                        )
                    }
                }
                AppDestinations.EDIT_EXPENSE -> {

                    val providers by providersViewModel
                        .getAllProviders()
                        .collectAsState(initial = emptyList())

                    val expenseTypes by expensesViewModel
                        .getAllExpenseTypes()
                        .collectAsState(initial = emptyList())

                    tripToEdit?.let { trip ->

                        ExpenseFormScreen(
                            trip = trip,
                            viewModel = expensesViewModel,
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding),
                            providers = providers,
                            expenseTypes = expenseTypes,
                            expenseToEdit = selectedExpenseToEdit,
                            onSave = {
                                selectedExpenseToEdit = null
                                currentDestination =
                                    AppDestinations.EDIT_TRIP
                            }
                        )
                    }
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

package io.github.jlrods.mytripsmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import io.github.jlrods.mytripsmanager.ui.theme.MyTripsManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTripsManagerTheme {
                MyTripsManagerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTripsManagerTopAppBar(modifier: Modifier = Modifier) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("My Trips") },
        modifier = modifier,
        actions = {
            IconButton(onClick = { /* TODO: Filter by name */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Filter by Name"
                )
            }
            IconButton(onClick = { /* TODO: Filter by country */ }) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Filter by Country"
                )
            }
            IconButton(onClick = { /* TODO: Filter by date */ }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Filter by Date Range"
                )
            }
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options"
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        /* TODO: Handle settings click */
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = {
                        /* TODO: Handle about click */
                        menuExpanded = false
                    }
                )
            }
        }
    )
}


@PreviewScreenSizes
@Composable
fun MyTripsManagerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TRIPS) }

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
            MainScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}


enum class AppDestinations(
    val label: String,
    val icon: Int, // drawable resource ID
) {
    TRIPS("Trips", icon = (R.drawable.ic_trip)),
    CITIES("Cities", R.drawable.ic_city),
    PROVIDERS("Providers", R.drawable.ic_provider,)
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Welcome to My Trips Manager!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTripsManagerTheme {
        MyTripsManagerApp()
    }
}

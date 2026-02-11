package io.github.jlrods.mytripsmanager.ui.components

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

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
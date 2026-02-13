package io.github.jlrods.mytripsmanager.ui.navigation

import io.github.jlrods.mytripsmanager.R

enum class AppDestinations(
    val label: String,
    val icon: Int, // drawable resource ID
    val showInBottomNav: Boolean = true
) {
    TRIPS("Trips", icon = (R.drawable.ic_trip),true),
    CITIES("Cities", R.drawable.ic_city,true),
    PROVIDERS("Providers", R.drawable.ic_provider,true),
    ADD_CITY("Add City", R.drawable.ic_city,false)
}
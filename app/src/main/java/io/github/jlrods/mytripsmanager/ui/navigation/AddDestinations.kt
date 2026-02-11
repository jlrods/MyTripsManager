package io.github.jlrods.mytripsmanager.ui.navigation

import io.github.jlrods.mytripsmanager.R

enum class AppDestinations(
    val label: String,
    val icon: Int, // drawable resource ID
) {
    TRIPS("Trips", icon = (R.drawable.ic_trip)),
    CITIES("Cities", R.drawable.ic_city),
    PROVIDERS("Providers", R.drawable.ic_provider)
}
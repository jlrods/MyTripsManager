package io.github.jlrods.mytripsmanager.ui.navigation

import io.github.jlrods.mytripsmanager.R

enum class AppDestinations(
    val label: String,
    val icon: Int, // drawable resource ID
    val showInBottomNav: Boolean = true
) {
    TRIPS("Trips", icon = (R.drawable.ic_trip),true),
    ADD_TRIP(label = "Add Trip", icon = R.drawable.ic_trip, showInBottomNav = false),
    EDIT_TRIP(label = "Edit Trip",icon = R.drawable.ic_trip, showInBottomNav = false),


    CITIES("Cities", R.drawable.ic_city,true),
    PROVIDERS("Providers", R.drawable.ic_provider,true),
    ADD_CITY("Add City", R.drawable.ic_city,false),

    EDIT_CITY("Edit City", R.drawable.ic_city,false),

    ADD_PROVIDER(label = "Add Provider",icon = R.drawable.ic_provider,showInBottomNav = false),

    EDIT_PROVIDER(label = "Edit Provider",icon = R.drawable.ic_provider,showInBottomNav = false),

    ADD_DESTINATION(label = "Add Destination",icon = R.drawable.ic_destination,showInBottomNav = false)
}
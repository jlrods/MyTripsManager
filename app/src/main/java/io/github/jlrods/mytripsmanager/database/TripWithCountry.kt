package io.github.jlrods.mytripsmanager.database

data class TripWithCountry(
    val trip: Trip,
    val countryName: String,
    val flagRes: Int
)
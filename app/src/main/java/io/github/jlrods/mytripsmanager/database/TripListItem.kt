package io.github.jlrods.mytripsmanager.database

data class TripListItem(
    val id: Int,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val countryName: String,
    val flagRes: Int
)
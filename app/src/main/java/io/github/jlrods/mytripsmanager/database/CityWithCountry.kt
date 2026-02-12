package io.github.jlrods.mytripsmanager.database

import androidx.room.Embedded
import androidx.room.Relation

data class CityWithCountry(
    @Embedded val city: City,

    @Relation(
        parentColumn = "countryId",
        entityColumn = "id"
    )
    val country: Country
)

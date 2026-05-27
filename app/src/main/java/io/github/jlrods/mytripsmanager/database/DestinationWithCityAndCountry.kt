package io.github.jlrods.mytripsmanager.database

import androidx.room.Embedded
import androidx.room.Relation

data class DestinationWithCityAndCountry(
    @Embedded
    val destination: Destination,

    @Relation(
        entity = City::class,
        parentColumn = "cityId",
        entityColumn = "id"
    )
    val cityWithCountry: CityWithCountry
)
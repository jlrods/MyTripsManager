package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.Destination
import io.github.jlrods.mytripsmanager.database.DestinationDao

class DestinationRepository(
    private val destinationDao: DestinationDao
) {

    suspend fun insert(destination: Destination) {
        destinationDao.insert(destination)
    }

    suspend fun insertDestination(
        tripId: Int,
        cityId: Int,
        start: Long,
        end: Long
    ) {

        destinationDao.insert(
            Destination(
                tripId = tripId,
                cityId = cityId,
                start =start,
                end = end
            )
        )
    }
}
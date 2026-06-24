package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.Destination
import io.github.jlrods.mytripsmanager.database.DestinationDao

class DestinationRepository(
    private val destinationDao: DestinationDao
) {

suspend fun insert(destination: Destination) {

    val previousDestination =
        destinationDao.getPreviousDestination(
            tripId = destination.tripId,
            newStart = destination.start
        )

    previousDestination?.let { previous ->

        destinationDao.update(
            previous.copy(
                end = destination.start
            )
        )
    }

    destinationDao.insert(destination)
    }

    suspend fun delete(destination: Destination) {
        destinationDao.delete(destination)
    }

    suspend fun updateDestination(
        destination: Destination
    ) {
        destinationDao.update(destination)
    }

}
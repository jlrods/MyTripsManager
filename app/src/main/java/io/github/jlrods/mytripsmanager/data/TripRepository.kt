package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.Trip
import io.github.jlrods.mytripsmanager.database.TripDao
import io.github.jlrods.mytripsmanager.database.TripListItem
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {

    val allTrips: Flow<List<TripListItem>> =
    tripDao.getTripListItems()
    suspend fun findByName(name: String): Trip? {
        return tripDao.getTripByName(name)
    }

    suspend fun insert(trip: Trip): Long {
        return tripDao.insert(trip)
    }

    suspend fun update(trip: Trip) =
        tripDao.update(trip = trip)

    suspend fun delete(trip: Trip) =
        tripDao.delete(trip = trip)

    fun getTripById(id: Int): Flow<Trip?> {
        return tripDao.getTripById(id)
    }
}
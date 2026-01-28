package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {
    @Query("SELECT * FROM destinations ORDER BY start ASC")
    fun getAllDestinations(): Flow<List<Destination>>

    @Query("SELECT * FROM destinations WHERE id = :id")
    fun getDestination(id: Int): Flow<Destination>

    @Query("SELECT * FROM destinations WHERE tripId = :tripId ORDER BY start ASC")
    fun getDestinationsByTrip(tripId: Int): Flow<List<Destination>>

    @Query("SELECT * FROM destinations WHERE cityId = :cityId ORDER BY start ASC")
    fun getDestinationsByCity(cityId: Int): Flow<List<Destination>>

    @Query("""
        SELECT d.* FROM destinations as d
        INNER JOIN cities as c ON d.cityId = c.id
        WHERE c.countryId = :countryId
        ORDER BY d.start ASC
    """)
    fun getDestinationsByCountry(countryId: Int): Flow<List<Destination>>

    @Query("SELECT * FROM destinations WHERE start BETWEEN :startDate AND :endDate ORDER BY start ASC")
    fun getDestinationsInDateRange(startDate: Long, endDate: Long): Flow<List<Destination>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(destination: Destination)

    @Update
    suspend fun update(destination: Destination)

    @Delete
    suspend fun delete(destination: Destination)
}
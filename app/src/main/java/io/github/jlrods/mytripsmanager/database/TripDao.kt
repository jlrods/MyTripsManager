package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY name ASC")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTrip(id: Int): Flow<Trip>

    @Query("SELECT * FROM trips WHERE name = :name LIMIT 1")
    suspend fun getTripByName(name: String): Trip?

    @Query("SELECT * FROM trips WHERE totalCost > :cost ORDER BY name ASC")
    fun getTripsWithCostGreaterThan(cost: Double): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE totalCost < :cost ORDER BY name ASC")
    fun getTripsWithCostLessThan(cost: Double): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE totalCost BETWEEN :minCost AND :maxCost ORDER BY name ASC")
    fun getTripsWithCostBetween(minCost: Double, maxCost: Double): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE start BETWEEN :startDate AND :endDate ORDER BY start ASC")
    fun getTripsBetweenDates(startDate: Long, endDate: Long): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE start > :date ORDER BY start ASC")
    fun getTripsAfterDate(date: Long): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE start < :date ORDER BY start ASC")
    fun getTripsBeforeDate(date: Long): Flow<List<Trip>>

    @Query("""
        SELECT trips.* FROM trips
        INNER JOIN destinations ON trips.id = destinations.tripId
        WHERE destinations.cityId = :cityId
    """)
    fun getTripsByDestination(cityId: Int): Flow<List<Trip>>

    @Query("""
        SELECT trips.* FROM trips
        INNER JOIN destinations ON trips.id = destinations.tripId
        INNER JOIN cities ON destinations.cityId = cities.id
        WHERE cities.countryId = :countryId
    """)
    fun getTripsByCountry(countryId: Int): Flow<List<Trip>>


    @Query("""
    SELECT
        trips.id AS id,
        trips.name AS name,
        trips.start AS startDate,
        trips.`end` AS endDate,
        countries.name AS countryName,
        countries.flagRes AS flagRes
    FROM trips
    INNER JOIN destinations
        ON trips.id = destinations.tripId
    INNER JOIN cities
        ON destinations.cityId = cities.id
    INNER JOIN countries
        ON cities.countryId = countries.id
    GROUP BY trips.id
    ORDER BY trips.start ASC
""")
    fun getTripListItems(): Flow<List<TripListItem>>

    @Query("SELECT * FROM trips WHERE cashSpent > cashBudget ORDER BY name ASC")
    fun getTripsWithOverspentBudget(): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE cashSpent <= cashBudget ORDER BY name ASC")
    fun getTripsWithUnderOrEqualBudget(): Flow<List<Trip>>

    @Query("SELECT COUNT(*) FROM trips")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)
}
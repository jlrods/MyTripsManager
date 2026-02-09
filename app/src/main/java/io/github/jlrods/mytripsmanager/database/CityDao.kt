package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCities(): Flow<List<City>>

    @Query("SELECT * FROM cities WHERE id = :id")
    fun getCity(id: Int): Flow<City>

    @Query("SELECT * FROM cities WHERE countryId = :countryId ORDER BY name ASC")
    fun getCitiesByCountry(countryId: Int): Flow<List<City>>

    @Query("SELECT * FROM cities WHERE name LIKE :name ORDER BY name ASC")
    fun getCitiesByName(name: String): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: City)

    @Update
    suspend fun update(city: City)

    @Delete
    suspend fun delete(city: City)
}
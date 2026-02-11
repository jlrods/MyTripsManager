package io.github.jlrods.mytripsmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries")
    suspend fun getAllCountriesOnce(): List<Country>

    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun getAllCountries(): Flow<List<Country>>

    @Query("SELECT * FROM countries WHERE id = :id")
    fun getCountry(id: Int): Flow<Country>

    @Query("SELECT * FROM countries WHERE name = :name")
    fun getCountryByName(name: String): Flow<Country>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(country: Country)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(countries: List<Country>)

    @Update
    suspend fun update(country: Country)

    @Delete
    suspend fun delete(country: Country)
}
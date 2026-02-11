package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.City
import io.github.jlrods.mytripsmanager.database.CityDao
import kotlinx.coroutines.flow.Flow

class CityRepository(
    private val cityDao: CityDao
) {

    val allCities: Flow<List<City>> = cityDao.getAllCities()

    fun getCitiesByCountry(countryId: Int): Flow<List<City>> {
        return cityDao.getCitiesByCountry(countryId)
    }

    fun searchCitiesByName(name: String): Flow<List<City>> {
        return cityDao.getCitiesByName(name)
    }

    suspend fun insert(city: City) {
        cityDao.insert(city)
    }

    suspend fun update(city: City) {
        cityDao.update(city)
    }

    suspend fun delete(city: City) {
        cityDao.delete(city)
    }
}
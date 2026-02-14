package io.github.jlrods.mytripsmanager.data

import io.github.jlrods.mytripsmanager.database.City
import io.github.jlrods.mytripsmanager.database.CityDao
import io.github.jlrods.mytripsmanager.database.CityWithCountry
import io.github.jlrods.mytripsmanager.database.Country
import io.github.jlrods.mytripsmanager.database.CountryDao
import kotlinx.coroutines.flow.Flow

class CityRepository(
    private val cityDao: CityDao,
    private val countryDao: CountryDao
) {

    val allCities: Flow<List<CityWithCountry>> =
        cityDao.getCitiesWithCountry()

    fun getCitiesByCountry(countryId: Int): Flow<List<City>> {
        return cityDao.getCitiesByCountry(countryId)
    }

    fun searchCitiesByName(name: String): Flow<List<City>> {
        return cityDao.getCitiesByName(name)
    }

    fun getAllCountries(): Flow<List<Country>>{
        return countryDao.getAllCountries()
    }

    suspend fun insertCity(city: City) : Long {
        return cityDao.insert(city)
    }

    suspend fun update(city: City) {
        cityDao.update(city)
    }

    suspend fun delete(city: City) {
        cityDao.delete(city)
    }

}
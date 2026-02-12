package io.github.jlrods.mytripsmanager.ui.screens.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.CityRepository
import io.github.jlrods.mytripsmanager.database.City
import io.github.jlrods.mytripsmanager.database.CityWithCountry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val repository: CityRepository
) : ViewModel() {

    val cities: StateFlow<List<CityWithCountry>> =
        repository.allCities
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val countries = repository.getAllCountries()

    fun insertCity(name: String, countryId: Int) {
        viewModelScope.launch {
            repository.insert(
                City(
                    name = name,
                    countryId = countryId
                )
            )
        }
    }

}

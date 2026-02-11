package io.github.jlrods.mytripsmanager.ui.screens.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jlrods.mytripsmanager.data.CityRepository

class CitiesViewModelFactory(
    private val repository: CityRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CitiesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

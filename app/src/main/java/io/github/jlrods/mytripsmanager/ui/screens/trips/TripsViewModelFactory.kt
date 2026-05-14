package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jlrods.mytripsmanager.data.TripRepository

class TripsViewModelFactory(
    private val repository: TripRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TripsViewModel(repository) as T
    }
}
package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.TripRepository
import io.github.jlrods.mytripsmanager.database.Trip
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripsViewModel(
    private val repository: TripRepository
) : ViewModel() {

    val trips: StateFlow<List<Trip>> =
        repository.allTrips.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertTrip(
        name: String,
        startDate: Long,
        endDate: Long,
        onDuplicate: () -> Unit,
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch {

            val existing = repository.findByName(name.trim().lowercase())

            if (existing != null) {
                onDuplicate()
                return@launch
            }

            val id = repository.insert(
                Trip(
                    name = name.trim().lowercase(),
                    start = startDate,
                    end = endDate
                )
            )

            onSuccess(id)
        }
    }
}
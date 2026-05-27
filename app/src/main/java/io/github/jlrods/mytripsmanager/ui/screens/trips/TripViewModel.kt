package io.github.jlrods.mytripsmanager.ui.screens.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jlrods.mytripsmanager.data.DestinationRepository
import io.github.jlrods.mytripsmanager.data.TripRepository
import io.github.jlrods.mytripsmanager.database.Destination
import io.github.jlrods.mytripsmanager.database.DestinationWithCityAndCountry
import io.github.jlrods.mytripsmanager.database.Trip
import io.github.jlrods.mytripsmanager.database.TripListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripsViewModel(
    private val repository: TripRepository,
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    val trips: StateFlow<List<TripListItem>> =
        repository.allTrips.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _tripDestinations =
        MutableStateFlow<List<DestinationWithCityAndCountry>>(emptyList())

    val tripDestinations = _tripDestinations.asStateFlow()

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

    fun getTripById(id: Int) =
        repository.getTripById(id)

    fun loadDestinationsForTrip(tripId: Int) {

        viewModelScope.launch {

            repository
                .getDestinationsForTrip(tripId)
                .collect {
                    _tripDestinations.value = it
                }
        }
    }

    fun insertTripWithDestination(
        name: String,
        startDate: Long,
        endDate: Long,
        cityId: Int?,
        onDuplicate: () -> Unit,
        onMissingCity: () -> Unit,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            if (cityId == null) {
                onMissingCity()
                return@launch
            }

            val existing =
                repository.findByName(
                    name.trim().lowercase()
                )

            if (existing != null) {

                onDuplicate()

                return@launch
            }

            val tripId = repository.insert(
                Trip(
                    name = name.trim(),
                    start = startDate,
                    end = endDate
                )
            )

            if (tripId > 0) {

                destinationRepository.insert(
                    Destination(
                        tripId = tripId.toInt(),
                        cityId = cityId,
                        start = startDate,
                        end = endDate
                    )
                )

                onSuccess()
            }
        }
    }
}
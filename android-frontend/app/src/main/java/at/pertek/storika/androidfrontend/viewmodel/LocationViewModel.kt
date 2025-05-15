package at.pertek.storika.androidfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.pertek.storika.androidfrontend.dto.LocationDto
import at.pertek.storika.androidfrontend.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LocationUiState {
    object Loading : LocationUiState()
    data class Success(val locations: List<LocationDto>) : LocationUiState()
    data class Error(val message: String) : LocationUiState()
}

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val uiState: StateFlow<LocationUiState> get() = _uiState

    init {
        fetchLocations()
    }

    fun fetchLocations() {
        viewModelScope.launch {
            try {
                val locations = repository.getAll()
                _uiState.value = LocationUiState.Success(locations)
            } catch (e: Exception) {
                _uiState.value = LocationUiState.Error("Fehler beim Laden der Orte")
            }
        }
    }

    fun getLocationById(id: Long) {
        viewModelScope.launch {
            try {
                val location = repository.get(id)
                _uiState.value = LocationUiState.Success(listOf(location))
            } catch (e: Exception) {
                _uiState.value = LocationUiState.Error("Fehler beim Laden des Orts")
            }
        }
    }
}
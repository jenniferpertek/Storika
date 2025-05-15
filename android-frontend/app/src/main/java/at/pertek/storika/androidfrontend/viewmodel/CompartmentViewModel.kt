package at.pertek.storika.androidfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.pertek.storika.androidfrontend.dto.CompartmentDto
import at.pertek.storika.androidfrontend.repository.CompartmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CompartmentUiState {
    object Loading : CompartmentUiState()
    data class Success(val compartments: List<CompartmentDto>) : CompartmentUiState()
    data class Error(val message: String) : CompartmentUiState()
}

class CompartmentViewModel(
    private val repository: CompartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CompartmentUiState>(CompartmentUiState.Loading)
    val uiState: StateFlow<CompartmentUiState> get() = _uiState

    init {
        fetchCompartments()
    }

    fun fetchCompartments() {
        viewModelScope.launch {
            try {
                val compartments = repository.getAll()
                _uiState.value = CompartmentUiState.Success(compartments)
            } catch (e: Exception) {
                _uiState.value = CompartmentUiState.Error("Fehler beim Laden der Fächer")
            }
        }
    }

    fun getCompartmentById(id: Long) {
        viewModelScope.launch {
            try {
                val compartment = repository.get(id)
                _uiState.value = CompartmentUiState.Success(listOf(compartment))
            } catch (e: Exception) {
                _uiState.value = CompartmentUiState.Error("Fehler beim Laden des Faches")
            }
        }
    }

}
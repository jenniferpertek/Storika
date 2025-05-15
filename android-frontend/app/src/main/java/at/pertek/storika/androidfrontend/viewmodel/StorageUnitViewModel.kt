package at.pertek.storika.androidfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.pertek.storika.androidfrontend.dto.StorageUnitDto
import at.pertek.storika.androidfrontend.repository.StorageUnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StorageUnitUiState {
    object Loading : StorageUnitUiState()
    data class Success(val units: List<StorageUnitDto>) : StorageUnitUiState()
    data class Error(val message: String) : StorageUnitUiState()
}

class StorageUnitViewModel(
    private val repository: StorageUnitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StorageUnitUiState>(StorageUnitUiState.Loading)
    val uiState: StateFlow<StorageUnitUiState> get() = _uiState

    init {
        fetchStorageUnits()
    }

    fun fetchStorageUnits() {
        viewModelScope.launch {
            try {
                val units = repository.getAll()
                _uiState.value = StorageUnitUiState.Success(units)
            } catch (e: Exception) {
                _uiState.value = StorageUnitUiState.Error("Fehler beim Laden der Lagerorte")
            }
        }
    }

    fun getStorageUnitById(id: Long) {
        viewModelScope.launch {
            try {
                val unit = repository.get(id)
                _uiState.value = StorageUnitUiState.Success(listOf(unit))
            } catch (e: Exception) {
                _uiState.value = StorageUnitUiState.Error("Fehler beim Laden des Lagerorts")
            }
        }
    }
}
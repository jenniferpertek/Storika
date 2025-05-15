package at.pertek.storika.androidfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.pertek.storika.androidfrontend.dto.ItemDto
import at.pertek.storika.androidfrontend.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ItemUiState {
    object Loading : ItemUiState()
    data class Success(val items: List<ItemDto>) : ItemUiState()
    data class Error(val message: String) : ItemUiState()
}

class ItemViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> get() = _uiState

    init {
        fetchItems()
    }

    fun fetchItems() {
        viewModelScope.launch {
            try {
                val items = repository.getAll()
                _uiState.value = ItemUiState.Success(items)
            } catch (e: Exception) {
                _uiState.value = ItemUiState.Error("Fehler beim Laden der Items")
            }
        }
    }

    fun createItem(item: ItemDto) {
        viewModelScope.launch {
            try {
                repository.create(item)
                fetchItems()
            } catch (e: Exception) {
                _uiState.value = ItemUiState.Error("Fehler beim Erstellen des Items")
            }
        }
    }

    fun updateItem(itemId: Long, item: ItemDto) {
        viewModelScope.launch {
            try {
                repository.update(itemId, item)
                fetchItems()
            } catch (e: Exception) {
                _uiState.value = ItemUiState.Error("Fehler beim Aktualisieren des Items")
            }
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            try {
                repository.delete(itemId)
                fetchItems()
            } catch (e: Exception) {
                _uiState.value = ItemUiState.Error("Fehler beim Löschen des Items")
            }
        }
    }
}
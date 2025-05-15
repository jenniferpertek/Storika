package at.pertek.storika.androidfrontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.pertek.storika.androidfrontend.dto.CategoryDto
import at.pertek.storika.androidfrontend.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class Success(val categories: List<CategoryDto>) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> get() = _uiState

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getAll()
                _uiState.value = CategoryUiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = CategoryUiState.Error("Fehler beim Laden der Kategorien")
            }
        }
    }

    fun createCategory(category: CategoryDto) {
        viewModelScope.launch {
            try {
                repository.create(category)
                fetchCategories() // neu laden
            } catch (e: Exception) {
                _uiState.value = CategoryUiState.Error("Fehler beim Erstellen der Kategorie")
            }
        }
    }

    fun updateCategory(categoryId: Long, category: CategoryDto) {
        viewModelScope.launch {
            try {
                repository.update(categoryId, category)
                fetchCategories() // neu laden
            } catch (e: Exception) {
                _uiState.value = CategoryUiState.Error("Fehler beim Aktualisieren der Kategorie")
            }
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            try {
                repository.delete(categoryId)
                fetchCategories() // neu laden
            } catch (e: Exception) {
                _uiState.value = CategoryUiState.Error("Fehler beim Löschen der Kategorie")
            }
        }
    }
}

package me.augusto.composebookapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.augusto.composebookapp.data.BookDetails
import me.augusto.composebookapp.data.BookRepository

data class BookDetailsUiState(
    val bookDetails: BookDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class BookDetailsViewModel : ViewModel() {

    private val repository = BookRepository

    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    fun loadBookDetails(workId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = repository.getBookDetails(workId)
            result.fold(
                onSuccess = { details ->
                    _uiState.update {
                        it.copy(isLoading = false, bookDetails = details)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            )
        }
    }
}

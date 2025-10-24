package me.augusto.composebookapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.augusto.composebookapp.data.Book
import me.augusto.composebookapp.data.BookRepository

data class BookUiState(
    val searchQuery: String = "",
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = "Search for a book to begin!"
)

class BookViewModel : ViewModel() {
    private val repository = BookRepository

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun searchBooks() {
        if (_uiState.value.searchQuery.isBlank()) return

        _uiState.update {
            it.copy(
                isLoading = true,
                infoMessage = null,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            // Call the repository object
            val result = repository.searchBooks(_uiState.value.searchQuery)

            result.fold(
                onSuccess = { books ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            books = books,
                            infoMessage = if (books.isEmpty()) "No matching books found :c" else null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "An unknown error occurred"
                        )
                    }
                }
            )
        }
    }
}
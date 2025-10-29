package me.augusto.composebookapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import me.augusto.composebookapp.data.Book
import me.augusto.composebookapp.presentation.theme.BookSearchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookSearchAppTheme {
                Scaffold { innerPadding ->
                    MainMenu(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainMenu(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var selectedBook: Book? by rememberSaveable { mutableStateOf(null) }

        if (selectedBook == null) {
            BookSearchScreen(
                onBookClick = { book ->
                    selectedBook = book
                }
            )
        } else {
            BookDetailScreen(
                book = selectedBook!!,
                onBackClick = {
                    selectedBook = null
                }
            )

            BackHandler {
                selectedBook = null
            }
        }
    }
}
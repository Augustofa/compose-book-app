package me.augusto.composebookapp.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.augusto.composebookapp.data.Book
import me.augusto.composebookapp.presentation.theme.BookSearchAppTheme
import java.text.DecimalFormat
import me.augusto.composebookapp.R

@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier,
    onBookClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {onBookClick()},
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.height(150.dp)
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(2f / 3f),
                 placeholder = painterResource(id = R.drawable.image_not_found),
                 error = painterResource(id = R.drawable.image_not_found),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.author ?: "Unknown Author",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = book.publicationYear?.toString() ?: "N/A",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    book.averageRating?.let {
                        BookRating(rating = it, modifier = Modifier.padding(end = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BookRating(rating: Double, modifier: Modifier = Modifier) {
    val ratingFormat = DecimalFormat("#.#")
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = Color(0xFFFFC107),
            modifier = Modifier.height(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = ratingFormat.format(rating),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookItemPreview() {
    BookSearchAppTheme {
        BookItem(
            book = Book(
                title = "The Lord of the Rings: The Fellowship of the Ring",
                author = "J.R.R. Tolkien",
                publicationYear = 1954,
                coverUrl = "",
                workId = "1",
                averageRating = 4.567
            ),
            modifier = Modifier.padding(16.dp),
            onBookClick = {}
        )
    }
}

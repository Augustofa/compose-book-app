package me.augusto.composebookapp.data

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
object BookRepository {
    private val apiService: BookApiService = RetrofitClient.apiService

    suspend fun searchBooks(query: String): Result<List<Book>> {
        return try {
            val response = apiService.searchBooks(query)
            if (!response.isSuccessful) {
                return Result.failure(Exception("Search failed: ${response.message()}"))
            }

            val documents = response.body()?.docs ?: emptyList()

            val booksWithRatings = coroutineScope {
                documents.map { doc ->
                    async {
                        val rating = fetchRating(doc.workId)
                        Book(
                            workId = doc.workId,
                            title = doc.title,
                            author = doc.authorName?.firstOrNull(),
                            publicationYear = doc.publishYear,
                            coverUrl = doc.coverId?.let {
                                "https://covers.openlibrary.org/b/id/$it-L.jpg"
                            },
                            averageRating = rating
                        )
                    }
                }.map { it.await() }
            }
            Result.success(booksWithRatings)

        } catch (e: Exception) {
            Log.e("BookRepository", "Error searching books", e)
            Result.failure(e)
        }
    }

    private suspend fun fetchRating(workId: String): Double? {
        return try {
            val response = apiService.getRating(workId)
            if (response.isSuccessful) {
                response.body()?.summary?.average
            } else {
                Log.w("BookRepository", "Failed to fetch rating for $workId: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("BookRepository", "Network error fetching rating for $workId", e)
            null
        }
    }
}


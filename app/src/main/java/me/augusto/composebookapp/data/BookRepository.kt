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

    suspend fun getBookDetails(workId: String): Result<BookDetails> {
        return try {
            val cleanedWorkId = workId.replace("/works/", "")

            val response = apiService.getBookDetails(cleanedWorkId)
            if (!response.isSuccessful) {
                return Result.failure(Exception("Failed to get details: ${response.message()}"))
            }

            val details = response.body()
            if (details == null) {
                return Result.failure(Exception("No details found"))
            }

            val cleanDetails = BookDetails(
                workId = workId,
                title = details.title,
                description = details.description?: "No description available.",
                subjects = details.subjects?.take(10) ?: emptyList(),
                coverUrl = details.coverIds?.firstOrNull()?.let {
                    "https://covers.openlibrary.org/b/id/$it-L.jpg"
                }
            )
            Result.success(cleanDetails)

        } catch (e: Exception) {
            Log.e("BookRepository", "Error getting book details", e)
            Result.failure(e)
        }
    }
}


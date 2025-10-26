package me.augusto.composebookapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("lang") lang: String = "pt"
    ): Response<OpenLibraryResponse>

    @GET("{workId}/ratings.json")
    suspend fun getRating(
        @Path(value = "workId", encoded = true) workId: String
    ): Response<RatingResponse>

    @GET("works/{workId}.json")
    suspend fun getBookDetails(
        @Path("workId") workId: String
    ): Response<BookDetailsResponse>
}

package me.augusto.composebookapp.data

import com.google.gson.annotations.SerializedName

// --- Search Request ---

data class OpenLibraryResponse(
    @SerializedName("docs")
    val docs: List<BookDocument>
)

data class BookDocument(
    @SerializedName("title")
    val title: String,

    @SerializedName("author_name")
    val authorName: List<String>?,

    @SerializedName("first_publish_year")
    val publishYear: Int?,

    @SerializedName("cover_i")
    val coverId: Int?,

    @SerializedName("key")
    val workId: String
)


// --- Rating Request ---

data class RatingResponse(
    @SerializedName("summary")
    val summary: RatingValue?
)

data class RatingValue(
    @SerializedName("average")
    val average: Double?
)
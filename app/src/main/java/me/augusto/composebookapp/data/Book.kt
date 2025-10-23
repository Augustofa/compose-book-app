package me.augusto.composebookapp.data

data class Book(
    val title: String,
    val author: String?,
    val publicationYear: Int?,
    val coverUrl: String?,
    val workId: String,
    val averageRating: Double?
)
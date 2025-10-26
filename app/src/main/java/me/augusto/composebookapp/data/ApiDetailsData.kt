package me.augusto.composebookapp.data

import com.google.gson.annotations.SerializedName

data class BookDetailsResponse(
    val title: String,
    val description: Description?,
    val subjects: List<String>?,
    @SerializedName("covers")
    val coverIds: List<Int>?
)

data class Description(
    val value: String
)

data class BookDetails(
    val workId: String,
    val title: String,
    val description: String,
    val subjects: List<String>,
    val coverUrl: String?
)
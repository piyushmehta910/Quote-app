package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    val id: String,
    val text: String,
    val author: String,
    val category: QuoteCategory,
    val isFavorite: Boolean = false
) : Parcelable

enum class QuoteCategory(val displayName: String) {
    MOTIVATION("Motivation"),
    SUCCESS("Success"),
    LIFE("Life"),
    CONFIDENCE("Confidence"),
    STUDY("Study"),
    FITNESS("Fitness"),
    BUSINESS("Business"),
    LOVE("Love"),
    FRIENDSHIP("Friendship")
}

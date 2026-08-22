package com.example.quoteapp.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.data.QuoteLibrary
import com.example.quoteapp.model.Quote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    val favoriteQuoteIds = ProjectRepository.favoriteQuoteIds
    val favoriteTemplateIds = ProjectRepository.favoriteTemplateIds

    private val _favoriteQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val favoriteQuotes: StateFlow<List<Quote>> = _favoriteQuotes.asStateFlow()

    fun loadFavoriteQuotes() {
        val ids = favoriteQuoteIds.value
        _favoriteQuotes.value = QuoteLibrary.getAll().filter { it.id in ids }
    }

    fun toggleFavoriteQuote(quoteId: String) {
        ProjectRepository.toggleFavoriteQuote(quoteId)
        loadFavoriteQuotes()
    }
}

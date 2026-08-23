package com.example.quoteapp.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.data.QuoteLibrary
import com.example.quoteapp.data.TemplateLibrary
import com.example.quoteapp.model.Quote
import com.example.quoteapp.model.QuoteTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    val favoriteQuoteIds = ProjectRepository.favoriteQuoteIds
    val favoriteTemplateIds = ProjectRepository.favoriteTemplateIds

    private val _favoriteQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val favoriteQuotes: StateFlow<List<Quote>> = _favoriteQuotes.asStateFlow()

    private val _favoriteTemplates = MutableStateFlow<List<QuoteTemplate>>(emptyList())
    val favoriteTemplates: StateFlow<List<QuoteTemplate>> = _favoriteTemplates.asStateFlow()

    fun loadFavoriteQuotes() {
        val ids = favoriteQuoteIds.value
        _favoriteQuotes.value = QuoteLibrary.getAll().filter { it.id in ids }
    }

    fun loadFavoriteTemplates() {
        val ids = favoriteTemplateIds.value
        _favoriteTemplates.value = TemplateLibrary.getAll().filter { it.id in ids }
    }

    fun toggleFavoriteQuote(quoteId: String) {
        ProjectRepository.toggleFavoriteQuote(quoteId)
        loadFavoriteQuotes()
    }
}

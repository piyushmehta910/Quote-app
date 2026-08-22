package com.example.quoteapp.ui.templates

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.data.TemplateLibrary
import com.example.quoteapp.model.QuoteTemplate
import com.example.quoteapp.model.TemplateCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemplatesViewModel(application: Application) : AndroidViewModel(application) {

    private val allTemplates = TemplateLibrary.getAll()

    private val _templates = MutableStateFlow<List<QuoteTemplate>>(allTemplates)
    val templates: StateFlow<List<QuoteTemplate>> = _templates.asStateFlow()

    private val _selectedCategory = MutableStateFlow<TemplateCategory?>(null)
    val selectedCategory: StateFlow<TemplateCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val favoriteTemplateIds = ProjectRepository.favoriteTemplateIds

    fun filterByCategory(category: TemplateCategory?) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun search(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun toggleFavorite(templateId: String) {
        ProjectRepository.toggleFavoriteTemplate(templateId)
    }

    private fun applyFilters() {
        var filtered = allTemplates

        _selectedCategory.value?.let { cat ->
            filtered = filtered.filter { it.category == cat }
        }

        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.displayName.contains(query, ignoreCase = true)
            }
        }

        _templates.value = filtered
    }
}

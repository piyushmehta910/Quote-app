package com.example.quoteapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.data.QuoteLibrary
import com.example.quoteapp.data.TemplateLibrary
import com.example.quoteapp.model.Project
import com.example.quoteapp.model.Quote
import com.example.quoteapp.model.QuoteTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    init {
        ProjectRepository.init(application)
    }

    val recentProjects: StateFlow<List<Project>> = ProjectRepository.projects

    private val _randomQuote = MutableStateFlow<Quote?>(null)
    val randomQuote: StateFlow<Quote?> = _randomQuote.asStateFlow()

    fun getRandomQuote() {
        _randomQuote.value = QuoteLibrary.getRandom()
    }

    fun getRecentProjects(count: Int = 5): List<Project> {
        return ProjectRepository.getRecentProjects(count)
    }

    fun deleteProject(id: String) {
        ProjectRepository.deleteProject(id)
    }
}

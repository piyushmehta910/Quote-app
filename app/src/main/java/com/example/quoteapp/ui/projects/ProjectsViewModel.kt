package com.example.quoteapp.ui.projects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.model.Project
import kotlinx.coroutines.flow.StateFlow

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    val projects: StateFlow<List<Project>> = ProjectRepository.projects

    fun deleteProject(id: String) {
        ProjectRepository.deleteProject(id)
    }

    fun duplicateProject(project: Project): Project {
        return ProjectRepository.duplicateProject(project)
    }

    fun renameProject(id: String, newName: String) {
        ProjectRepository.renameProject(id, newName)
    }

    fun toggleFavorite(id: String) {
        ProjectRepository.toggleFavoriteProject(id)
    }

    fun getFavoriteProjects(): List<Project> {
        return ProjectRepository.getFavoriteProjects()
    }
}

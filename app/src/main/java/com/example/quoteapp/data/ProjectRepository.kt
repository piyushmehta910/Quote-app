package com.example.quoteapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.quoteapp.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ProjectRepository {

    private const val PREFS_NAME = "quote_app_projects"
    private const val KEY_PROJECTS = "projects"
    private const val KEY_FAVORITE_TEMPLATES = "favorite_templates"
    private const val KEY_FAVORITE_QUOTES = "favorite_quotes"
    private const val KEY_FAVORITE_BACKGROUNDS = "favorite_backgrounds"

    private val gson = Gson()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _favoriteTemplateIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteTemplateIds: StateFlow<Set<String>> = _favoriteTemplateIds.asStateFlow()

    private val _favoriteQuoteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteQuoteIds: StateFlow<Set<String>> = _favoriteQuoteIds.asStateFlow()

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadProjects()
        loadFavorites()
    }

    private fun getPrefs(): SharedPreferences {
        return prefs ?: throw IllegalStateException("ProjectRepository not initialized. Call init(context) first.")
    }

    fun saveProject(project: Project) {
        val current = _projects.value.toMutableList()
        val index = current.indexOfFirst { it.id == project.id }
        val updated = project.copy(updatedAt = System.currentTimeMillis())
        if (index >= 0) {
            current[index] = updated
        } else {
            current.add(0, updated)
        }
        _projects.value = current
        persistProjects()
    }

    fun getProject(id: String): Project? = _projects.value.find { it.id == id }

    fun deleteProject(id: String) {
        _projects.value = _projects.value.filter { it.id != id }
        persistProjects()
    }

    fun duplicateProject(project: Project): Project {
        val newProject = project.copy(
            id = "proj_${System.currentTimeMillis()}",
            name = "${project.name} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        saveProject(newProject)
        return newProject
    }

    fun renameProject(id: String, newName: String) {
        val current = _projects.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index >= 0) {
            current[index] = current[index].copy(
                name = newName,
                updatedAt = System.currentTimeMillis()
            )
            _projects.value = current
            persistProjects()
        }
    }

    fun toggleFavoriteProject(id: String) {
        val current = _projects.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index >= 0) {
            current[index] = current[index].copy(
                isFavorite = !current[index].isFavorite,
                updatedAt = System.currentTimeMillis()
            )
            _projects.value = current
            persistProjects()
        }
    }

    fun getRecentProjects(count: Int = 10): List<Project> {
        return _projects.value.sortedByDescending { it.updatedAt }.take(count)
    }

    fun getFavoriteProjects(): List<Project> {
        return _projects.value.filter { it.isFavorite }
    }

    fun toggleFavoriteTemplate(templateId: String) {
        val current = _favoriteTemplateIds.value.toMutableSet()
        if (current.contains(templateId)) current.remove(templateId) else current.add(templateId)
        _favoriteTemplateIds.value = current
        persistFavorites(KEY_FAVORITE_TEMPLATES, current)
    }

    fun toggleFavoriteQuote(quoteId: String) {
        val current = _favoriteQuoteIds.value.toMutableSet()
        if (current.contains(quoteId)) current.remove(quoteId) else current.add(quoteId)
        _favoriteQuoteIds.value = current
        persistFavorites(KEY_FAVORITE_QUOTES, current)
    }

    private fun persistProjects() {
        val json = gson.toJson(_projects.value)
        getPrefs().edit().putString(KEY_PROJECTS, json).apply()
    }

    private fun loadProjects() {
        val json = getPrefs().getString(KEY_PROJECTS, null)
        if (json != null) {
            val type = object : TypeToken<List<Project>>() {}.type
            _projects.value = try { gson.fromJson(json, type) ?: emptyList() } catch (e: Exception) { emptyList() }
        }
    }

    private fun persistFavorites(key: String, ids: Set<String>) {
        getPrefs().edit().putString(key, gson.toJson(ids)).apply()
    }

    private fun loadFavorites() {
        val templateJson = getPrefs().getString(KEY_FAVORITE_TEMPLATES, null)
        if (templateJson != null) {
            val type = object : TypeToken<Set<String>>() {}.type
            _favoriteTemplateIds.value = try { gson.fromJson(templateJson, type) ?: emptySet() } catch (e: Exception) { emptySet() }
        }
        val quoteJson = getPrefs().getString(KEY_FAVORITE_QUOTES, null)
        if (quoteJson != null) {
            val type = object : TypeToken<Set<String>>() {}.type
            _favoriteQuoteIds.value = try { gson.fromJson(quoteJson, type) ?: emptySet() } catch (e: Exception) { emptySet() }
        }
    }

    fun generateId(): String = "proj_${System.currentTimeMillis()}_${(Math.random() * 1000).toInt()}"
}

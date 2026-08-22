package com.example.quoteapp.ui.editor

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteapp.data.ProjectRepository
import com.example.quoteapp.data.TemplateLibrary
import com.example.quoteapp.model.*
import com.example.quoteapp.util.EditorHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val history = EditorHistory()

    private val _state = MutableStateFlow(EditorState())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    private val _exportComplete = MutableStateFlow<Boolean?>(null)
    val exportComplete: StateFlow<Boolean?> = _exportComplete.asStateFlow()

    private var currentProjectId: String? = null

    init {
        history.push(_state.value)
        updateHistoryState()
    }

    fun loadProject(projectId: String) {
        val project = ProjectRepository.getProject(projectId) ?: return
        currentProjectId = project.id
        val newState = EditorState(
            canvasSize = project.canvasSize,
            aspectRatio = AspectRatios.fromCanvasSize(project.canvasSize),
            quote = project.quote,
            author = project.author,
            source = project.source,
            background = project.background,
            quoteStyle = project.quoteStyle,
            authorStyle = project.authorStyle,
            overlay = project.overlay,
            exportSettings = project.exportSettings
        )
        _state.value = newState
        history.reset(newState)
        updateHistoryState()
    }

    fun loadTemplate(templateId: String) {
        val template = TemplateLibrary.getById(templateId) ?: return
        updateState {
            it.copy(
                template = template,
                background = template.background,
                quoteStyle = template.quoteStyle,
                authorStyle = template.authorStyle,
                overlay = template.overlay,
                decorations = template.decorations
            )
        }
    }

    fun updateQuote(text: String) {
        updateState { it.copy(quote = text) }
    }

    fun updateAuthor(text: String) {
        updateState { it.copy(author = text) }
    }

    fun updateSource(text: String) {
        updateState { it.copy(source = text) }
    }

    fun updateQuoteStyle(style: TextSettings) {
        updateState { it.copy(quoteStyle = style) }
    }

    fun updateAuthorStyle(style: TextSettings) {
        updateState { it.copy(authorStyle = style) }
    }

    fun updateBackground(background: QuoteBackground) {
        updateState { it.copy(background = background) }
    }

    fun updateOverlay(overlay: OverlaySettings) {
        updateState { it.copy(overlay = overlay) }
    }

    fun updateAspectRatio(ratio: AspectRatio) {
        val canvasSize = CanvasSize.fromAspectRatio(ratio)
        updateState {
            it.copy(
                aspectRatio = ratio,
                canvasSize = canvasSize
            )
        }
    }

    fun updateCanvasSize(width: Int, height: Int) {
        val size = CanvasSize(width, height)
        val ratio = AspectRatios.fromCanvasSize(size)
        updateState {
            it.copy(
                canvasSize = size,
                aspectRatio = ratio
            )
        }
    }

    fun updateExportSettings(settings: ExportSettings) {
        updateState { it.copy(exportSettings = settings) }
    }

    fun setActiveTab(tab: EditorTab) {
        _state.value = _state.value.copy(activeTab = tab)
    }

    fun setActiveTextTarget(target: TextTarget) {
        _state.value = _state.value.copy(activeTextTarget = target)
    }

    fun updateCurrentTextStyle(style: TextSettings) {
        when (_state.value.activeTextTarget) {
            TextTarget.QUOTE -> updateQuoteStyle(style)
            TextTarget.AUTHOR -> updateAuthorStyle(style)
        }
    }

    fun applyTextStylePreset(preset: TextStylePreset) {
        when (_state.value.activeTextTarget) {
            TextTarget.QUOTE -> updateQuoteStyle(preset.settings)
            TextTarget.AUTHOR -> updateAuthorStyle(preset.settings)
        }
    }

    fun undo() {
        val prev = history.undo() ?: return
        _state.value = prev
        updateHistoryState()
    }

    fun redo() {
        val next = history.redo() ?: return
        _state.value = next
        updateHistoryState()
    }

    fun reset() {
        val initial = EditorState()
        _state.value = initial
        history.reset(initial)
        updateHistoryState()
    }

    fun loadBackgroundImage(uri: Uri) {
        val background = QuoteBackground.Image(uri = uri.toString())
        updateState { it.copy(background = background) }
    }

    fun saveProject(name: String? = null): String {
        val s = _state.value
        val id = currentProjectId ?: ProjectRepository.generateId()
        currentProjectId = id
        val project = Project(
            id = id,
            name = name ?: "Project ${_state.value.quote.take(20)}",
            quote = s.quote,
            author = s.author,
            source = s.source,
            templateId = s.template?.id,
            canvasSize = s.canvasSize,
            background = s.background,
            quoteStyle = s.quoteStyle,
            authorStyle = s.authorStyle,
            overlay = s.overlay,
            exportSettings = s.exportSettings
        )
        ProjectRepository.saveProject(project)
        return id
    }

    fun setExporting(exporting: Boolean) {
        _isExporting.value = exporting
    }

    fun setExportComplete(success: Boolean) {
        _exportComplete.value = success
        _isExporting.value = false
    }

    fun clearExportResult() {
        _exportComplete.value = null
    }

    private fun updateState(transform: (EditorState) -> EditorState) {
        val newState = transform(_state.value)
        _state.value = newState
        history.push(newState)
        updateHistoryState()
    }

    private fun updateHistoryState() {
        _canUndo.value = history.canUndo
        _canRedo.value = history.canRedo
    }
}

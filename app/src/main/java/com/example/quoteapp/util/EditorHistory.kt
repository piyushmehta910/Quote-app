package com.example.quoteapp.util

import com.example.quoteapp.model.EditorState

class EditorHistory(
    private val maxHistory: Int = 50
) {
    private val history = mutableListOf<EditorState>()
    private var currentIndex = -1

    val canUndo: Boolean get() = currentIndex > 0
    val canRedo: Boolean get() = currentIndex < history.size - 1

    fun push(state: EditorState) {
        if (currentIndex < history.size - 1) {
            history.subList(currentIndex + 1, history.size).clear()
        }
        history.add(state)
        if (history.size > maxHistory) {
            history.removeAt(0)
        } else {
            currentIndex++
        }
    }

    fun undo(): EditorState? {
        if (!canUndo) return null
        currentIndex--
        return history[currentIndex]
    }

    fun redo(): EditorState? {
        if (!canRedo) return null
        currentIndex++
        return history[currentIndex]
    }

    fun reset(initialState: EditorState) {
        history.clear()
        history.add(initialState)
        currentIndex = 0
    }

    fun getCurrent(): EditorState? {
        return if (currentIndex in history.indices) history[currentIndex] else null
    }
}

package com.kodebug.firebasenoteapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodebug.firebasenoteapp.data.model.Note
import com.kodebug.firebasenoteapp.data.remote.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val isLoading = _loading.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            repository.getNotes().collect { noteList ->
                _notes.value = noteList
                _loading.value = false
            }
        }
    }

    fun addNote(title: String, description: String) {
        viewModelScope.launch {
            val newNoteId = repository.newNoteId
            val note = Note(
                id = newNoteId,
                title = title,
                description = description
            )
            repository.addNote(note)
        }
    }

    fun updateNote(oldNoteId: String, title: String, description: String) {
        viewModelScope.launch {
            val updatedNote = Note(
                id = oldNoteId,
                title = title,
                description = description
            )
            repository.updateNote(updatedNote)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun getNoteById(noteId: String, onResult: (Note?) -> Unit) {
        viewModelScope.launch {
            val note = repository.getNoteById(noteId)
            onResult(note)
        }
    }
}
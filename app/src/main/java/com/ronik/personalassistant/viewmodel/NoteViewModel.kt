package com.ronik.personalassistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ronik.personalassistant.model.Note
import com.ronik.personalassistant.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(
    app: Application, private val noteRepository: NoteRepository
): AndroidViewModel(app) {

    fun add(note: Note) =
        viewModelScope.launch {
            noteRepository.add(note)
        }

    fun update(note: Note) =
        viewModelScope.launch {
            noteRepository.update(note)
        }

    fun delete(note: Note) =
        viewModelScope.launch {
            noteRepository.delete(note)
        }

    fun all() = noteRepository.all()

    fun allWithSearch(search: String?) = noteRepository.allWithSearch(search)
}
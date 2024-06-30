package com.ronik.personalassistant.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ronik.personalassistant.repository.NoteRepository

class NoteViewModelFactory(
    val app: Application, private val noteRepository: NoteRepository
): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(this.app, this.noteRepository) as T
    }
}
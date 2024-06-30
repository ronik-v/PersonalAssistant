package com.ronik.personalassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ronik.personalassistant.database.NoteData
import com.ronik.personalassistant.repository.NoteRepository
import com.ronik.personalassistant.viewmodel.NoteViewModel
import com.ronik.personalassistant.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel() {
        val noteRepository = NoteRepository(NoteData(this))
        val viewModelProvidedFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProvidedFactory)[NoteViewModel::class.java]
    }
}
package com.ronik.personalassistant.repository

import com.ronik.personalassistant.database.NoteData
import com.ronik.personalassistant.database.Notes
import com.ronik.personalassistant.model.Note

class NoteRepository(
    private val db: NoteData
): Notes {
    // Crud методы для to-do заметок
    override suspend fun add(note: Note) = this.db.notes().add(note)
    override suspend fun update(note: Note) = this.db.notes().update(note)
    override suspend fun delete(note: Note) = this.db.notes().delete(note)

    override fun all() = this.db.notes().all()
    override fun allWithSearch(search: String?) = this.db.notes().allWithSearch(search)
}
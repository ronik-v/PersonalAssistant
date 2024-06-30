package com.ronik.personalassistant.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.ronik.personalassistant.MainActivity
import com.ronik.personalassistant.R
import com.ronik.personalassistant.databinding.FragmentEditNoteBinding
import com.ronik.personalassistant.model.Note
import com.ronik.personalassistant.viewmodel.NoteViewModel


class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {
    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note
    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        this.notesViewModel = (activity as MainActivity).noteViewModel
        this.currentNote = this.args.note!!

        this.binding.editNoteTitle.setText(this.currentNote.title)
        this.binding.editNoteDesc.setText(this.currentNote.description)
        this.binding.editNoteFab.setOnClickListener {
            val title = this.binding.editNoteTitle.text.toString().trim()
            val description = this.binding.editNoteDesc.text.toString().trim()

            if (title.isNotEmpty()) {
                val updatedNote = Note(this.currentNote.id, title, description)
                this.notesViewModel.update(updatedNote)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Ошибка обновления - укажите заголовок", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun delete() {
        AlertDialog.Builder(activity).apply {
            setTitle("Удаление заметки")
            setMessage("Вы хотите удалить заметку?")
            setPositiveButton("Удалить") {
                _,_ -> notesViewModel.delete(currentNote)
                Toast.makeText(context, "Заметка - ${currentNote.title} удаленна", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Отменить", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                this.delete()
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.editNoteBinding = null
    }
}
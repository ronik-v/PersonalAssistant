package com.ronik.personalassistant.fragments

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
import com.ronik.personalassistant.MainActivity
import com.ronik.personalassistant.R
import com.ronik.personalassistant.databinding.FragmentAddNoteBinding
import com.ronik.personalassistant.model.Note
import com.ronik.personalassistant.viewmodel.NoteViewModel


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNotesView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        this.notesViewModel = (activity as MainActivity).noteViewModel
        this.addNotesView = view
    }

    private fun add(view: View) {
        val title = this.binding.addNoteTitle.text.toString().trim()
        val description = this.binding.addNoteDesc.text.toString().trim()

        if (title.isNotEmpty()) {
            val note = Note(0, title, description)
            this.notesViewModel.add(note)

            Toast.makeText(this.addNotesView.context, "Заметка сохраненна", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(this.addNotesView.context, "Ошибка сохранения - отсутствует заголовок", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                this.add(this.addNotesView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.addNoteBinding = null
    }
}
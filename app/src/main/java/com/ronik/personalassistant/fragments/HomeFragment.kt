package com.ronik.personalassistant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ronik.personalassistant.MainActivity
import com.ronik.personalassistant.R
import com.ronik.personalassistant.adapter.NoteAdapter
import com.ronik.personalassistant.databinding.FragmentHomeBinding
import com.ronik.personalassistant.model.Note
import com.ronik.personalassistant.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var notesAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return this.homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        this.notesViewModel = (activity as MainActivity).noteViewModel
        this.setupRecyclerView()

        this.binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun updateView(note: List<Note>) {
        if (note.isNotEmpty()) {
            this.binding.homeRecyclerView.visibility = View.VISIBLE
        } else {
            this.binding.homeRecyclerView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        this.notesAdapter = NoteAdapter()
        this.binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = notesAdapter
        }

        activity?.let {
            this.notesViewModel.all().observe(viewLifecycleOwner) {
                note -> notesAdapter.diff.submitList(note)
                this.updateView(note)
            }
        }
    }

    private fun searchNote(search: String?) {
        val searchString = "%$search"
        this.notesViewModel.allWithSearch(searchString).observe(this) {
            list -> this.notesAdapter.diff.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isNotEmpty()) {
            this.searchNote(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        this.homeBinding = null
    }
}
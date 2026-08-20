package com.example.mynotetaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mynotetaker.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment(R.layout.fragment_note_list) {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNoteListBinding.bind(view)

        adapter = NoteAdapter(
            onItemClick = { note -> navigateToAddEdit(note.id) },
            onDeleteClick = { note -> confirmDelete(note) }
        )

        binding.recyclerView.adapter = adapter
        binding.fabAdd.setOnClickListener { navigateToAddEdit(null) }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        NoteRepository.loadNotes(requireContext())
        val currentNotes = NoteRepository.notes
        adapter.submitList(currentNotes)
        binding.tvEmptyState.visibility = if (currentNotes.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun confirmDelete(note: Note) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                NoteRepository.delete(requireContext(), note.id)
                refreshUI()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToAddEdit(noteId: Int?) {
        val fragment = AddEditNoteFragment().apply {
            arguments = Bundle().apply { putInt("NOTE_ID", noteId ?: -1) }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
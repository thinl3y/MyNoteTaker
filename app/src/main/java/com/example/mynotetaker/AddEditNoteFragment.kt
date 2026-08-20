package com.example.mynotetaker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mynotetaker.databinding.FragmentAddEditNoteBinding

class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {
    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!
    private var noteId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditNoteBinding.bind(view)

        noteId = arguments?.getInt("NOTE_ID", -1) ?: -1
        if (noteId != -1) {
            val existingNote = NoteRepository.getNote(noteId)
            existingNote?.let { note ->
                binding.etTitle.setText(note.title)
                binding.etContent.setText(note.content)
            }
        }

        binding.btnSave.setOnClickListener { saveNote() }
        binding.btnCancel.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Title and Content cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        NoteRepository.saveOrUpdate(
            context = requireContext(),
            id = if (noteId == -1) null else noteId,
            title = title,
            content = content
        )

        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
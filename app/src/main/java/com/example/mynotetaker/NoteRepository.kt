package com.example.mynotetaker

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object NoteRepository {
    val notes = mutableListOf<Note>()
    private const val PREFS_NAME = "note_app_prefs"
    private const val KEY_NOTES = "saved_notes"

    fun loadNotes(context: Context) {
        notes.clear()
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_NOTES, null) ?: return

        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val title = obj.getString("title")
                val content = obj.getString("content")
                notes.add(Note(id, title, content))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToPreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray()

        for (note in notes) {
            val obj = JSONObject().apply {
                put("id", note.id)
                put("title", note.title)
                put("content", note.content)
            }
            jsonArray.put(obj)
        }

        prefs.edit().putString(KEY_NOTES, jsonArray.toString()).apply()
    }

    fun getNote(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun saveOrUpdate(context: Context, id: Int?, title: String, content: String) {
        val existing = notes.find { it.id == id }
        if (existing != null) {
            existing.title = title
            existing.content = content
        } else {
            notes.add(Note(title = title, content = content))
        }
        saveToPreferences(context)
    }

    fun delete(context: Context, id: Int) {
        notes.removeAll { it.id == id }
        saveToPreferences(context)
    }
}
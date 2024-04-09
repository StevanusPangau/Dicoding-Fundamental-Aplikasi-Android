package com.learn.dicodingmynotesapp.helper

import android.database.Cursor
import com.learn.dicodingmynotesapp.db.DatabaseContract.NoteColumns
import com.learn.dicodingmynotesapp.entity.Note

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(NoteColumns.TITLE))
                val description =
                    getString(getColumnIndexOrThrow(NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(NoteColumns.DATE))
                notesList.add(Note(id, title, description, date))
            }
        }
        return notesList
    }

}
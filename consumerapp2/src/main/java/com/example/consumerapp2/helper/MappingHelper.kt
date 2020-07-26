package com.example.consumerapp2.helper

import android.database.Cursor
import com.example.consumerapp2.data.DataUserItems
import com.example.consumerapp2.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<DataUserItems> {
        val favList = ArrayList<DataUserItems>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR_URL))
                favList.add(DataUserItems(id, username, avatar))
            }
        }
        return favList
    }
}
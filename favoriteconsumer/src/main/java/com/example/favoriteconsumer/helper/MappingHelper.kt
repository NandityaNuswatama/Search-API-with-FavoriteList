package com.example.favoriteconsumer.helper

import android.database.Cursor
import com.example.favoriteconsumer.data.DataUserItems
import com.example.favoriteconsumer.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<DataUserItems> {
        val favList = ArrayList<DataUserItems>()

        favCursor?.apply {
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
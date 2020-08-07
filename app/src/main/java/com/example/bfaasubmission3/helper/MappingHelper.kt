package com.example.bfaasubmission3.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.example.bfaasubmission3.data.DataUserItems
import com.example.bfaasubmission3.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<DataUserItems> {
        val favList = ArrayList<DataUserItems>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR_URL))
                favList.add(DataUserItems(id = id, username = username, avatar = avatar))
            }
        }
        return favList
    }

    fun mapCursorToObject(cursor: Cursor?): DataUserItems {
        var favUser = DataUserItems()
        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR_URL))
            favUser = DataUserItems(id = id, username = username, avatar = avatar)
        }
        return favUser
    }
}
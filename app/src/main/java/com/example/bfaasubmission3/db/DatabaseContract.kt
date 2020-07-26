package com.example.bfaasubmission3.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {
    class FavColumns: BaseColumns{
        companion object{
            const val _ID = "_id"
            const val TABLE_NAME = "favorite_user"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"

            const val AUTHORITY = "com.example.bfaasubmission3"
            const val SCHEME = "content"

            val CONTENT_URI: Uri? = Uri.Builder().scheme(SCHEME)
                .authority(com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.AUTHORITY)
                .appendPath(com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME)
                .build()
        }
    }
}
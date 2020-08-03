package com.example.bfaasubmission3.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val SCHEME = "content"
    const val AUTHORITY = "com.example.bfaasubmission3"

    class FavColumns: BaseColumns{
        companion object{
            const val _ID = "_id"
            const val TABLE_NAME = "favorite_user"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri? = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
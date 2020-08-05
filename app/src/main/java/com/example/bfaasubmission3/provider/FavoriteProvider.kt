package com.example.bfaasubmission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.bfaasubmission3.db.DatabaseContract.AUTHORITY
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.bfaasubmission3.db.FavHelper
import timber.log.Timber

class FavoriteProvider: ContentProvider() {

    companion object{
        private const val FAVORITE = 1
        private const val FAVORITE_USERNAME = 2
        private lateinit var favHelper: FavHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_USERNAME)
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        Timber.i("query: $uri")
        Timber.i("uriMatcher: ${sUriMatcher.match(uri)}")
        when(sUriMatcher.match(uri)){
            FAVORITE -> cursor = favHelper.queryAll()
            else -> cursor = favHelper.queryByUsername(uri.lastPathSegment.toString())
        }
        Timber.i("cursor: $cursor")
        return cursor
    }

    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(FAVORITE){
            sUriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }

        if (CONTENT_URI != null) {
            context?.contentResolver?.notifyChange(CONTENT_URI, null)
        }
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val delete: Int = when(FAVORITE_USERNAME){
            sUriMatcher.match(uri) -> favHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        if (CONTENT_URI != null) {
            context?.contentResolver?.notifyChange(CONTENT_URI, null)
        }
        return delete
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}
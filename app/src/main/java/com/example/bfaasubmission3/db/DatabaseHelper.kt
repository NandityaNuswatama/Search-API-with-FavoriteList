package com.example.bfaasubmission3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {
    companion object{
        private const val DATABASE_NAME = "favlist"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavColumns.USERNAME} TEXT NOT NULL," +
                "${DatabaseContract.FavColumns.AVATAR_URL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
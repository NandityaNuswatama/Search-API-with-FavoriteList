package com.example.bfaasubmission3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.AVATAR_URL
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.USERNAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "dbfavlist"
        private const val DATABASE_VERSION = 10
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME"+
                "($_ID INTEGER," +
                "$USERNAME TEXT NOT NULL," +
                "$AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
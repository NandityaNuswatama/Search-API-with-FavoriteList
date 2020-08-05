package com.example.bfaasubmission3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion._ID

class FavHelper(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private lateinit var db: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavHelper? = null

        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE
                    ?: FavHelper(context)
            }
    }

    fun open(){
        db = dbHelper.writableDatabase
    }

    fun queryAll(): Cursor =
        db.query(DATABASE_TABLE, null, null, null, null, null, null, null)

    fun queryByUsername(username: String): Cursor =
        db.query(DATABASE_TABLE, null, "$USERNAME = ?", arrayOf(username), null, null, null)

    fun insert(values: ContentValues?): Long =
        db.insert(DATABASE_TABLE, null, values)

    fun deleteByUsername(username: String): Int =
        db.delete(DATABASE_TABLE, "$_ID = $username", null)


}
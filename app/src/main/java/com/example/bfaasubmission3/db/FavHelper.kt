package com.example.bfaasubmission3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.bfaasubmission3.db.DatabaseContract.FavColumns.Companion._ID

class FavHelper(context: Context) {
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavHelper? = null

        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE
                    ?: FavHelper(context)
            }
    }

    init {
        databaseHelper =
            DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}
package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class MyDbManager (context: Context) {

    private val myDbHelper = MyDbHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase
    }
    fun insertToDb(result:String, path:String)
    {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, result)
            put(MyDbNameClass.COLUMN_NAME_PATH, path)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }
}
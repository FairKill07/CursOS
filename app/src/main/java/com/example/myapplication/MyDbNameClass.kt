package com.example.myapplication

import android.provider.BaseColumns

object MyDbNameClass : BaseColumns {
    const val TABLE_NAME = "my_data"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_PATH = "path"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "MyDb.db"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_NAME_TITLE TEXT," +
                "$COLUMN_NAME_PATH TEXT)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
}
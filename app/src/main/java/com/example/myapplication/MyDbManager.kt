package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class MyDbManager (val context: Context) {
    val myDbHelper = MyDbHelper(context);

    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase
    }
    fun insertToDb(result:String, path:String)
    {
        var values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, result)
            put(MyDbNameClass.COLUMN_NAME_PATH, path)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

//    @SuppressLint("Range")
//    fun readDb(): ArrayList<String>{
//        val db : SQLiteDatabase? = myDbHelper.writableDatabase
//        val dataList = ArrayList<String>();
//
//        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null,null,null,null,null,null, null)
//
//        while (cursor?.moveToNext() !!)
//        {
//                val dataText = cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
//                val dataPath = cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_PATH))
//                Log.d("title", dataText.toString());
//                Log.d("path", dataPath.toString())
//                dataList.add(dataText.toString());
//
//
//        }
//
//
//        cursor?.close()
////        db.delete(MyDbNameClass.TABLE_NAME, null,null)
//        db.close();
//        return dataList
//    }


}
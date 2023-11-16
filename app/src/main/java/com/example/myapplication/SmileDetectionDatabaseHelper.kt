import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SmileDetectionDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "smileDetectionHistory.db"

        const val TABLE_HISTORY = "smile_history"
        const val COLUMN_ID = "_id"
        const val COLUMN_RESULT = "result"
        const val COLUMN_PHOTO_PATH = "photo_path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_HISTORY_TABLE = ("CREATE TABLE $TABLE_HISTORY ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_RESULT TEXT, $COLUMN_PHOTO_PATH TEXT)")
        db.execSQL(CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    fun addHistory(result: String, photoPath: String) {
        val values = ContentValues()
        values.put(COLUMN_RESULT, result)
        values.put(COLUMN_PHOTO_PATH, photoPath)

        val db = this.writableDatabase
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }

    fun getAllHistory(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_HISTORY", null)
    }
}

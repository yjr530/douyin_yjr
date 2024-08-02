package com.xiaomi.douyin_yjr.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create table for user data
        db.execSQL("CREATE TABLE $TABLE_USER (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ACCOUNT TEXT, " +
                "$COLUMN_PASSWORD TEXT)")

        // Create table for video metadata
        db.execSQL("CREATE TABLE $TABLE_VIDEO (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_FILEPATH TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VIDEO")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DouYin.db"

        // User table
        const val TABLE_USER = "user"
        const val COLUMN_ID = "id"
        const val COLUMN_ACCOUNT = "account"
        const val COLUMN_PASSWORD = "password"

        // Video table
        const val TABLE_VIDEO = "video"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_FILEPATH = "filepath"
    }
}
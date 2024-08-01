package com.xiaomi.douyin_yjr.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri

class VideoDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addVideo(title: String, description: String, filepath: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, title)
            put(DatabaseHelper.COLUMN_DESCRIPTION, description)
            put(DatabaseHelper.COLUMN_FILEPATH, filepath)
        }
        db.insert(DatabaseHelper.TABLE_VIDEO, null, values)
        db.close()
    }

    fun getVideos(): List<Video> {
        val videos = mutableListOf<Video>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_VIDEO,
            arrayOf(DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_FILEPATH),
            null, null, null, null, null
        )
        while (cursor?.moveToNext() == true) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION))
            val filepath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILEPATH))
            videos.add(Video(title, null, Uri.parse(filepath))) // Adjust as necessary
        }
        cursor?.close()
        db.close()
        return videos
    }

    fun clearVideos() {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_VIDEO, null, null)
        db.close()
    }
}
package com.xiaomi.douyin_yjr.data

import android.content.ContentValues
import android.content.Context

class UserDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addUser(account: String, password: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ACCOUNT, account)
            put(DatabaseHelper.COLUMN_PASSWORD, password)
        }
        db.insert(DatabaseHelper.TABLE_USER, null, values)
        db.close()
    }

    fun getUser(account: String): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USER,
            arrayOf(DatabaseHelper.COLUMN_PASSWORD),
            "${DatabaseHelper.COLUMN_ACCOUNT}=?",
            arrayOf(account),
            null, null, null
        )
        val password = cursor?.let {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
            } else {
                null
            }
        }
        cursor?.close()
        db.close()
        return password
    }
}
package com.example.sentrycallnew

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "SentryDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE whitelist (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT UNIQUE," +
                    "phone TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS whitelist")
        onCreate(db)
    }

    fun insertContact(name: String, phone: String) {

        val hashedName = HashUtil.getSecureHash(name)

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM whitelist WHERE name = ?",
            arrayOf(hashedName)
        )

        val exists = cursor.count > 0
        cursor.close()

        if (!exists) {
            val writeDb = writableDatabase
            val values = ContentValues()
            values.put("name", hashedName)
            values.put("phone", phone)
            writeDb.insert("whitelist", null, values)
        }
    }

    fun getAllContacts(): List<String> {
        val list = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM whitelist", null)

        while (cursor.moveToNext()) {
            list.add(cursor.getString(0))
        }

        cursor.close()
        return list
    }
    fun isContactInDb(name: String?): Boolean {

        if (name == null) return false

        val hashedName = HashUtil.getSecureHash(name)

        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM whitelist WHERE name = ?",
            arrayOf(hashedName)
        )

        val exists = cursor.count > 0
        cursor.close()

        return exists
    }
}
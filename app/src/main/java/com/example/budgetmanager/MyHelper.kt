package com.example.budgetmanager

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context?) : SQLiteOpenHelper(context, "transactions", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "day INTEGER," +
                    "month INTEGER," +
                    "year INTEGER," +
                    "source TEXT," +
                    "amount DOUBLE," +
                    "category TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}
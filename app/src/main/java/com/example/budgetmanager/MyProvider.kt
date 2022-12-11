package com.example.budgetmanager

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class MyProvider : ContentProvider() {

    companion object {
        val PROVIDER_NAME = "package com.example.budgetmanager/MyProvider"
        val TABLE_NAME = "transactions"
        val URL = "content://$PROVIDER_NAME/$TABLE_NAME"
        val CONTENT_URI: Uri = Uri.parse(URL)

        val ID = "id"
        val DAY = "day"
        val MONTH = "month"
        val YEAR = "year"
        val SOURCE = "source"
        val AMOUNT = "amount"
        val CATEGORY = "category"
    }

    lateinit var db : SQLiteDatabase

    override fun onCreate(): Boolean {
        val helper = MyHelper(context)
        db = helper.writableDatabase
        return db != null
    }

    override fun query(
        uri: Uri,
        cols: Array<out String>?,
        condition: String?,
        conditionValue: Array<out String>?,
        order: String?
    ): Cursor? {
        return db.query("transactions", cols, condition, conditionValue, null, null, order)
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.example.transactions"
    }

    override fun insert(uri: Uri, cv: ContentValues?): Uri? {
        db.insert("transactions", null, cv)
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun delete(uri: Uri, condition: String?, condictionValue: Array<out String>?): Int {
        var count = db.delete("transactions", condition, condictionValue)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun update(uri: Uri, cv: ContentValues?, condiction: String?, condictionValue: Array<out String>?): Int {
        var count = db.update("transactions",cv,condiction,condictionValue)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }
}
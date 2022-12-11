package com.example.budgetmanager

import androidx.room.*

@Dao
interface TransactionDAO {

    @Query("SELECT * FROM transactions")
    fun getAll() : List<Transaction>

    @Insert
    fun insertAll(vararg transaction : Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)

}
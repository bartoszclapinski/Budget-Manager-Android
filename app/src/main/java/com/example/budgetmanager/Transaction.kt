package com.example.budgetmanager

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val source: String,
    val amount: Double,
    val category: String) : Serializable {
}
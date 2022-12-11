package com.example.budgetmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransactionDelete : AppCompatActivity() {

    private lateinit var transaction : Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_delete)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        val noButton = findViewById<MaterialButton>(R.id.noButton)
        val yesButton = findViewById<MaterialButton>(R.id.yesButton)

        noButton.setOnClickListener {
            finish()
        }

        yesButton.setOnClickListener {
            val db = Room.databaseBuilder(this, Database::class.java, "transactions").build()
            GlobalScope.launch {
                db.transactionDAO().delete(transaction)
                finish()
            }
        }
    }
}
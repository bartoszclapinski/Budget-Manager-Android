package com.example.budgetmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_add)

        val sourceInput = findViewById<TextView>(R.id.sourceInput)
        val amountInput = findViewById<TextView>(R.id.amountInput)
        val descInput = findViewById<TextView>(R.id.descriptionInput)
        val dateInput = findViewById<DatePicker>(R.id.datePicker)

        val sourceLayout = findViewById<TextInputLayout>(R.id.sourceLayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.amountLayout)
        val descLayout = findViewById<TextInputLayout>(R.id.descriptionLayout)

        val closeButton = findViewById<ImageButton>(R.id.closeButton)

        sourceInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                sourceLayout.error = null
        }
        amountInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                amountLayout.error = null
        }
        descInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                descLayout.error = null
        }

        val button = findViewById<MaterialButton>(R.id.addTransactionBtn)
        button.setOnClickListener{

            val source = sourceInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()
            val desc = descInput.text.toString()
            val day = dateInput.dayOfMonth
            val month = dateInput.month
            val year = dateInput.year

            if (source.isEmpty()) {
                sourceLayout.error = "Wprowadź Źródło"
            } else if (amount == null) {
                amountLayout.error = "Wprowadź Prawidłową Kwotę"
            } else if (desc.isEmpty()) {
                descLayout.error = "Wprowadź Kategorię"
            } else {
                val transaction = Transaction(0, day, month, year, source, amount, desc)
                insert(transaction)
            }
        }

        closeButton.setOnClickListener{
            finish()
        }
    }

    private fun insert (transaction: Transaction) {
        val db = Room.databaseBuilder(this, Database::class.java, "transactions")
            .build()
        GlobalScope.launch {
            db.transactionDAO().insertAll(transaction)
            finish()
        }
    }

}
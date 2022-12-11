package com.example.budgetmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TransactionEdit : AppCompatActivity() {
    private lateinit var transaction : Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_edit)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        val sourceInput = findViewById<TextView>(R.id.sourceInput)
        val amountInput = findViewById<TextView>(R.id.amountInput)
        val descInput = findViewById<TextView>(R.id.descriptionInput)
        val dateInput = findViewById<DatePicker>(R.id.datePicker)

        val sourceLayout = findViewById<TextInputLayout>(R.id.sourceLayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.amountLayout)
        val descLayout = findViewById<TextInputLayout>(R.id.descriptionLayout)

        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        val updateButton = findViewById<MaterialButton>(R.id.updateTransactionBtn)
        val shareButton = findViewById<FloatingActionButton>(R.id.shareButton)

        val rootEditView = findViewById<ConstraintLayout>(R.id.rootEditView)

        sourceInput.text = transaction.source
        amountInput.text = transaction.amount.toString()
        descInput.text = transaction.category
        dateInput.init(transaction.year, transaction.month, transaction.day
        ) { _, _, _, _ ->
            updateButton.visibility = View.VISIBLE
        }

        rootEditView.setOnClickListener {
            this.window.decorView.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        sourceInput.addTextChangedListener {
            updateButton.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                sourceLayout.error = null
        }
        amountInput.addTextChangedListener {
            updateButton.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                amountLayout.error = null
        }
        descInput.addTextChangedListener {
            updateButton.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                descLayout.error = null
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            val dateString =
                String.format("%d.%d.%d",
                transaction.day, transaction.month, transaction.year)
            val shareString =
                String.format("Data: %s, Kategoria: %s, Źródło: %s, Kwota: %s",
                    dateString, transaction.category, transaction.source, transaction.amount)
            intent.putExtra(Intent.EXTRA_TEXT, shareString);
            val chooser = Intent.createChooser(intent, "Udostępnij za pomocą:")
            startActivity(chooser)
        }

        updateButton.setOnClickListener{

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
                val transaction = Transaction(transaction.id, day, month, year, source, amount, desc)
                update(transaction)
            }
        }

        closeButton.setOnClickListener{
            finish()
        }
    }

    private fun update (transaction: Transaction) {
        val db = Room.databaseBuilder(this, Database::class.java, "transactions")
            .build()
        GlobalScope.launch {
            db.transactionDAO().update(transaction)
            finish()
        }
    }

}
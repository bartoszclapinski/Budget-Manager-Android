package com.example.budgetmanager

import android.content.Intent
import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions : List<Transaction>
    private lateinit var oldTransactions : List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var transaction: Transaction

    private lateinit var db : Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf()
        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(this, Database::class.java, "transactions").build()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener{
            val intent = Intent(this, TransactionAdd::class.java)
            startActivity(intent)
        }


    }

    private fun fetchAll() {
        GlobalScope.launch {
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            transactions = db.transactionDAO().getAll()
                .filter { it.month == currentMonth && it.year == currentYear}
            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
            }
        }
    }

    private fun updateDashboard() {
        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount = transactions.filter{ it.amount > 0 }.map{ it.amount }.sum()
        val expenseAmount = totalAmount - budgetAmount

        val balance = findViewById<TextView>(R.id.balance)
        val budget = findViewById<TextView>(R.id.budget)
        val expense = findViewById<TextView>(R.id.expense)

        balance.text = "%.2f PLN".format(totalAmount)
        budget.text = "%.2f PLN".format(budgetAmount)
        expense.text = "%.2f PLN".format(expenseAmount)
    }

    private fun showSnackbar(){
        val view = findViewById<View>(R.id.coordinator)
        val snackbar = Snackbar.make(view, "Transaction deleted!",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.my_blue))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    private fun undoDelete(){
        GlobalScope.launch {
            db.transactionDAO().insertAll(deletedTransaction)

            transactions = oldTransactions

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            db.transactionDAO().delete(transaction)

            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
                showSnackbar()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}
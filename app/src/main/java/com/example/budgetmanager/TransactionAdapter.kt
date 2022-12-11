package com.example.budgetmanager

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount : TextView = view.findViewById(R.id.amount)
        val category : TextView = view.findViewById(R.id.description)
        val source : TextView = view.findViewById(R.id.source)
        val date : TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_layout, parent, false)
        
        return TransactionHolder(view);
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.amount.context

        if (transaction.amount >= 0) {
            holder.amount.text = "+ %.2f PLN".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.my_green))
        } else {
            holder.amount.text = "- %.2f PLN".format(abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.my_red))
        }

        holder.source.text = transaction.source
        holder.category.text = transaction.category
        holder.date.text = String.format("%d.%d.%d", transaction.day, transaction.month + 1, transaction.year)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, TransactionEdit::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener{
            val intent = Intent(context, TransactionDelete::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)
            true
        }

    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setData (transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }


}
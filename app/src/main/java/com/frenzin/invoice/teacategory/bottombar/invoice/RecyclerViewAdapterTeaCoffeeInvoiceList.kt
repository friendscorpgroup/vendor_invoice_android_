package com.frenzin.invoice.teacategory.bottombar.invoice

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frenzin.invoice.R
import com.frenzin.invoice.extras.Utils
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserDetailsModelTeaCoffee
import com.frenzin.invoice.roomdatabase.UserModel
import java.util.*

class RecyclerViewAdapterTeaCoffeeInvoiceList(
    var list: List<UserDetailsModelTeaCoffee>,
    val updateBillAmount: UpdateBillAmount
) :
    RecyclerView.Adapter<RecyclerViewAdapterTeaCoffeeInvoiceList.ViewHolder>() {

    lateinit var context: Context
    private var list1: List<UserModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_invoice_item_list_teacoffee, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Items = list[position]

        list1 = ArrayList()
        list1 = DatabaseClass.getDatabase(context)!!.dao!!.getCustomer(Items.userId!!.toInt())

        val totalTeaItem =Items.teaItemMrng!!.toDouble() + Items.teaItemEvng!!.toDouble()
        val totalCoffeeItem =Items.coffeeItemMrng!!.toDouble() + Items.coffeeItemEvng!!.toDouble()

        val columnTea = Utils.calculateTotalTea(list)
        val columnCoffee = Utils.calculateTotalCoffee(list)

        val totalInvoiceAmount = (columnTea * (list1!![0].teaprice!!.toDouble())) + (columnCoffee *(list1!![0].coffeprice!!.toDouble()))

        val teaTotalPrice = (totalTeaItem) * (list1!![0].teaprice!!.toDouble())
        val coffeeTotalPrice = (totalCoffeeItem) * (list1!![0].coffeprice!!.toDouble())
        val totalItemAmount = (teaTotalPrice + coffeeTotalPrice)

        holder.tvInvoiceNo.text = ((position + 1).toString())
        holder.tvInvoiceDate.text = Items.createdDate
        holder.tvInvoiceTypeTea.text = totalTeaItem.toInt().toString()
        holder.tvInvoiceTypeCoffee.text = totalCoffeeItem.toInt().toString()
        holder.tvInvoiceAmount.text = String.format("%.2f", totalItemAmount)

        updateBillAmount.updateBillAmount(totalInvoiceAmount)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInvoiceNo: TextView = view.findViewById(R.id.tvInvoiceNoC)
        val tvInvoiceDate: TextView = view.findViewById(R.id.tvInvoiceDateC)
        val tvInvoiceTypeTea: TextView = view.findViewById(R.id.tvInvoiceTypeTea)
        val tvInvoiceTypeCoffee: TextView = view.findViewById(R.id.tvInvoiceTypeCoffee)
        val tvInvoiceAmount: TextView = view.findViewById(R.id.tvInvoiceAmountC)
    }

}
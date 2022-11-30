package com.frenzin.invoice.watercategory.bottombar.invoice

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
import com.frenzin.invoice.roomdatabase.UserDetailsModelWater
import com.frenzin.invoice.roomdatabase.UserModel
//import com.frenzin.invoice.database.ModalClassWaterCustomerDetails
import com.frenzin.invoice.teacategory.bottombar.invoice.UpdateBillAmount
import java.util.ArrayList


class RecyclerViewAdapterWaterInvoiceList(
    val mList: List<UserDetailsModelWater>,
    val updateBillAmount: UpdateBillAmount
)  : RecyclerView.Adapter<RecyclerViewAdapterWaterInvoiceList.ViewHolder>() {

    lateinit var context: Context
    private var list1: List<UserModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_invoice_item_list_water, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Items = mList[position]

        list1 = ArrayList()
        list1 = DatabaseClass.getDatabase(context)!!.dao!!.getCustomer(Items.userId!!.toInt())

        val columnTotalBottle = Utils.calculateTotalWaterBottle(mList)
        val totalInvoiceAmount = columnTotalBottle * (list1!![0].waterprice!!.toDouble())

        val totalPrice = (Items.addbottle!!.toDouble()) * (list1!![0].waterprice!!.toDouble())

        holder.tvInvoiceNo.text = (position + 1).toString()
        holder.tvInvoiceDate.text = Items.createdDate
        holder.tvInvoiceQty.text = Items.addbottle.toString()
        holder.tvInvoiceAmount.text = String.format("%.2f", totalPrice)

        updateBillAmount.updateBillAmount(totalInvoiceAmount)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInvoiceNo: TextView = view.findViewById(R.id.tvInvoiceNo)
        val tvInvoiceDate: TextView = view.findViewById(R.id.tvInvoiceDate)
        val tvInvoiceQty: TextView = view.findViewById(R.id.tvInvoiceQty)
        val tvInvoiceAmount: TextView = view.findViewById(R.id.tvInvoiceAmount)

    }

}

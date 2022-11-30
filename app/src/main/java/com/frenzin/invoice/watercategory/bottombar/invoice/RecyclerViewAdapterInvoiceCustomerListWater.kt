package com.frenzin.invoice.watercategory.bottombar.invoice

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frenzin.invoice.R
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserDetailsModelWater
import com.frenzin.invoice.roomdatabase.UserModel

class RecyclerViewAdapterInvoiceCustomerListWater(
   private val mList: List<UserModel>?
) :
RecyclerView.Adapter<RecyclerViewAdapterInvoiceCustomerListWater.ViewHolder>() {

    private var list2: List<UserDetailsModelWater>? = null
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_invoice_customer_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Items = mList?.get(position)!!

        list2 = ArrayList()
        list2 = DatabaseClass.getDatabase(context)!!.dao!!.getCustomerDataWater(Items.key)

        holder.tvCustomerName.text = Items.fname
        holder.tvCustomerType.text = Items.type

        if (list2!!.size > 0){
            holder.BtnGenerateInvoice.visibility = View.VISIBLE
            holder.BtnGenerateInvoice.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(holder.itemView.context, InvoiceWaterInvoiceGenerateActivity::class.java)
                    intent.putExtra("id", Items.key.toString())
                    intent.putExtra("ofcNo", Items.officename.toString())
                    intent.putExtra("fName", Items.fname.toString())
                    holder.itemView.context.startActivity(intent)
                }
            })
        }
        else {
            holder.BtnGenerateInvoice.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCustomerName: TextView = view.findViewById(R.id.tvCustomerName)
        val tvCustomerType: TextView = view.findViewById(R.id.tvCustomerType)
        val BtnGenerateInvoice: TextView = view.findViewById(R.id.BtnGenerateInvoiceW)
    }

}

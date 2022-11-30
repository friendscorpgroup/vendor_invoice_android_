package com.frenzin.invoice.watercategory.bottombar.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frenzin.invoice.R
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserDetailsModelWater
import com.frenzin.invoice.roomdatabase.UserModel
import com.frenzin.invoice.teacategory.bottombar.dashboard.DeleteCustomer
//import com.frenzin.invoice.roomdatabase.CustomerTBL
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*


class RecyclerViewAdapterBottleCustomerList(
val mList: List<UserModel>?,
private val openAlertDialog: OpenAlertDialog,
private val deleteCustomer: DeleteCustomer
)  : RecyclerView.Adapter<RecyclerViewAdapterBottleCustomerList.ViewHolder>() {

    lateinit var context: Context
    private var list1: List<UserDetailsModelWater>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_recyclerview_customerlist_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Items = mList?.get(position)!!

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        list1 = ArrayList()
        list1 = DatabaseClass.getDatabase(context)!!.dao!!.getWaterBottleCountData(formattedDate, Items.key)!!

        var addItem = ""
        var emptyItem = ""

        if (list1!!.size > 0){
            addItem = list1!![0].addbottle.toString()
            emptyItem = list1!![0].emptybottle.toString()
        }
        else
        {
            addItem = "0"
            emptyItem = "0"
        }

        holder.tvNameW.text = Items.fname + " " + Items.lname
        holder.tvTotalBottleW.text = addItem
        holder.tvEmptyBottleW.text = emptyItem

        holder.addBottle.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                openAlertDialog.openAlertDialog(
                    Items.key.toString(),
                    Items.fname,
                    addItem.toInt(),
                    emptyItem.toInt(),
                    0,
                    0
                )
            }
        })

        holder.btnDeleteCustomerWater.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                showPopupMenu()
            }

            fun showPopupMenu() {
                val context: Context = ContextThemeWrapper(holder.itemView.context, R.style.RoundedCornersDialog)
                val popupMenu = PopupMenu(context, holder.btnDeleteCustomerWater)

                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                        if (menuItem.itemId.equals(R.id.delete)){
                            deleteCustomer.deleteCustomer(Items.key.toString())
                        }
                        if (menuItem.itemId.equals(R.id.edit)){
                            val intent = Intent(holder.itemView.context, AddCustomerWaterActivity::class.java)
                            intent.putExtra("key","EditCustomerWater")
                            intent.putExtra("fname", Items.fname)
                            intent.putExtra("lname", Items.lname)
                            intent.putExtra("officename", Items.officename)
                            intent.putExtra("phonenumber", Items.phonenumber)
                            intent.putExtra("address", Items.address)
                            intent.putExtra("waterprice", Items.waterprice)
                            intent.putExtra("type", Items.type)
                            intent.putExtra("userid", Items.key.toString())
                            holder.itemView.context.startActivity(intent)
                        }
                        return true
                    }
                })

                popupMenu.show()
            }
        })

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNameW = view.findViewById<TextView>(R.id.tvNameW)
        val tvTotalBottleW = view.findViewById<TextView>(R.id.tvTotalBottleW)
        val tvEmptyBottleW = view.findViewById<TextView>(R.id.tvEmptyBottleW)
        val addBottle = view.findViewById<MaterialButton>(R.id.addBottle)
        val cardViewWater = view.findViewById<MaterialCardView>(R.id.cardViewWater)
        val btnDeleteCustomerWater = view.findViewById<ImageView>(R.id.btnDeleteCustomerWater)
    }

}

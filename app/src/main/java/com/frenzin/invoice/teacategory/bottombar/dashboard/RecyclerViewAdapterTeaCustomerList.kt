package com.frenzin.invoice.teacategory.bottombar.dashboard

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
import com.frenzin.invoice.roomdatabase.UserDetailsModelTeaCoffee
import com.frenzin.invoice.roomdatabase.UserModel
import com.frenzin.invoice.watercategory.bottombar.dashboard.OpenAlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapterTeaCustomerList(
    var list: List<UserModel>,
    private val openAlertDialog: OpenAlertDialog,
    private val deleteCustomer: DeleteCustomer
) :
    RecyclerView.Adapter<RecyclerViewAdapterTeaCustomerList.ViewHolder>() {

    lateinit var context: Context
    private var list1: List<UserDetailsModelTeaCoffee>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_recyclerview_customerlist_tea, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val Items = list[position]

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        list1 = ArrayList()
        list1 = DatabaseClass.getDatabase(context)!!.dao!!.getTeaCoffeeCountData(formattedDate, Items.key)!!

        var teaItemMrng = ""
        var coffeeItemMrng = ""
        var teaItemEvng = ""
        var coffeeItemEvng = ""

        if (list1!!.size > 0){
            teaItemMrng = list1!![0].teaItemMrng.toString()
            coffeeItemMrng = list1!![0].coffeeItemMrng.toString()
            teaItemEvng = list1!![0].teaItemEvng.toString()
            coffeeItemEvng = list1!![0].coffeeItemEvng.toString()
        }
        else
        {
            teaItemMrng = "0"
            coffeeItemMrng = "0"
            teaItemEvng = "0"
            coffeeItemEvng = "0"
        }

        holder.tvNameW.text = Items.fname + " " + Items.lname
        holder.tvTeaCount.text = (teaItemMrng.toInt() + teaItemEvng.toInt()).toString()
        holder.tvCoffeeCount.text = (coffeeItemMrng.toInt() + coffeeItemEvng.toInt()).toString()

        holder.addTea.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                openAlertDialog.openAlertDialog(
                    Items.key.toString(),
                    Items.fname,
                    teaItemMrng.toInt(),
                    coffeeItemMrng.toInt(),
                    teaItemEvng.toInt(),
                    coffeeItemEvng.toInt())
            }
        })

        holder.btnDeleteCustomer.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                showPopupMenu()
            }

            fun showPopupMenu() {
                val context: Context = ContextThemeWrapper(holder.itemView.context, R.style.RoundedCornersDialog)
                val popupMenu = PopupMenu(context, holder.btnDeleteCustomer)

                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                        if (menuItem.itemId.equals(R.id.delete)){
                            deleteCustomer.deleteCustomer(Items.key.toString())
                        }
                        if (menuItem.itemId.equals(R.id.edit)){
                            val intent = Intent(holder.itemView.context, AddCustomerTeaActivity::class.java)
                            intent.putExtra("key","EditCustomer")
                            intent.putExtra("fname", Items.fname)
                            intent.putExtra("lname", Items.lname)
                            intent.putExtra("officename", Items.officename)
                            intent.putExtra("phonenumber", Items.phonenumber)
                            intent.putExtra("address", Items.address)
                            intent.putExtra("teaprice", Items.teaprice)
                            intent.putExtra("coffeprice", Items.coffeprice)
                            intent.putExtra("morningtime", Items.morningtime)
                            intent.putExtra("eveningtime", Items.eveningtime)
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
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNameW: TextView = view.findViewById(R.id.tvNameTea)
        val tvTeaCount: TextView = view.findViewById(R.id.tvTotalTea)
        val tvCoffeeCount: TextView = view.findViewById(R.id.tvTotalCoffee)
        val tvDrinkType: TextView = view.findViewById(R.id.tvDrinkType)
        val addTea: MaterialButton = view.findViewById(R.id.addTea)
        val cardViewTea: MaterialCardView = view.findViewById(R.id.cardViewTea)
        val btnDeleteCustomer: ImageView = view.findViewById(R.id.btnDeleteCustomer)
    }

}

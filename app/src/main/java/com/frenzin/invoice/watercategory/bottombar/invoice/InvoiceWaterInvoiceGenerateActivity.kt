package com.frenzin.invoice.watercategory.bottombar.invoice

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frenzin.invoice.R
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserDetailsModelWater
import com.frenzin.invoice.teacategory.bottombar.invoice.UpdateBillAmount
import java.text.SimpleDateFormat
import java.util.*

class InvoiceWaterInvoiceGenerateActivity : AppCompatActivity(), UpdateBillAmount {

    lateinit var recyclerViewInvoiceItems: RecyclerView
    lateinit var tvTotalAmount: TextView
    lateinit var tvCustomerName: TextView
    lateinit var tvOfficeNo: TextView
    lateinit var amountRs: TextView
    lateinit var billingStartDateWater: TextView
    lateinit var billingEndDateWater: TextView
    lateinit var datePickupStartWater: TextView
    lateinit var datePickupEndWater: TextView
    lateinit var datePickerEndLayoutWater: LinearLayout
    lateinit var datePickerStartLayoutWater: LinearLayout
    lateinit var backBtnGenerateInvoiceW: ImageView
    lateinit var rvCardViewLayout: CardView
    private var list1: List<UserDetailsModelWater>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_invoice_water)

        setFindViewById()
        setRecyclerView()
        onClickListener()
        initData()

    }

    override fun onResume() {
        super.onResume()
        initData()
        setRecyclerView()
    }

    private fun initData() {
        tvCustomerName.text = intent.getStringExtra("fName")
        tvOfficeNo.text = intent.getStringExtra("ofcNo")

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val dateStart = prefs1.getString("date", "1")!!.toInt()
        val monthStart = prefs1.getString("month", "$month")!!.toInt()
        val yearStart = prefs1.getString("year", "$year")!!.toInt()
        val dateEnd = prefs1.getString("dateEnd", "30")!!.toInt()
        val monthEnd = prefs1.getString("monthEnd", "$month")!!.toInt()
        val yearEnd = prefs1.getString("yearEnd", "$year")!!.toInt()

        val month_date = SimpleDateFormat("MMM")
        cal[Calendar.MONTH] = monthStart
        val monthStart1: String = month_date.format(cal.time)

        val month_dateEnd = SimpleDateFormat("MMM")
        cal[Calendar.MONTH] = monthEnd
        val monthEnd1: String = month_dateEnd.format(cal.time)

        val formattedDate = "$dateStart $monthStart1. $yearStart"
        val formattedDate1 = "$dateEnd $monthEnd1. $yearEnd"

        datePickupStartWater.text = formattedDate
        datePickupEndWater.text = formattedDate1
        billingStartDateWater.text = formattedDate
        billingEndDateWater.text = formattedDate1
    }

    private fun setFindViewById() {
        recyclerViewInvoiceItems = findViewById(R.id.recyclerViewInvoiceItems)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        backBtnGenerateInvoiceW = findViewById(R.id.backBtnGenerateInvoiceW)
        tvCustomerName = findViewById(R.id.tvCustomerName)
        tvOfficeNo = findViewById(R.id.tvOfficeNo)
        amountRs = findViewById(R.id.amountRs)
        billingEndDateWater = findViewById(R.id.billingEndDateWater)
        billingStartDateWater = findViewById(R.id.billingStartDateWater)
        datePickupEndWater = findViewById(R.id.datePickupEndWater)
        datePickupStartWater = findViewById(R.id.datePickupStartWater)
        datePickerEndLayoutWater = findViewById(R.id.datePickerEndLayoutWater)
        datePickerStartLayoutWater = findViewById(R.id.datePickerStartLayoutWater)
        rvCardViewLayout = findViewById(R.id.rvCardViewLayout)
    }

    private fun onClickListener() {
        backBtnGenerateInvoiceW.setOnClickListener(onClickListener)
        datePickerEndLayoutWater.setOnClickListener(onClickListener)
        datePickerStartLayoutWater.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.backBtnGenerateInvoiceW -> {
                    finish()
                }
                R.id.datePickerStartLayoutWater -> {
                    openCalenderStartDate()
                }
                R.id.datePickerEndLayoutWater -> {
                    openCalenderEndDate()
                }
            }
        }
    }


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun openCalenderStartDate() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val month1 = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val date11 = prefs1.getString("date", "1")!!.toInt()
        val month11 = prefs1.getString("month", "$month")!!.toInt()
        val year11 = prefs1.getString("year", "$year")!!.toInt()

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            datePickupStartWater.text = (" $dayOfMonth $month1. $year")
            billingStartDateWater.text = (" $dayOfMonth $month1. $year")
            prefs1.edit().putString("date", dayOfMonth.toString()).apply()
            prefs1.edit().putString("year", year.toString()).apply()
            prefs1.edit().putString("month", month.toString()).apply()
            initData()
            setRecyclerView()}, year11, month11, date11)

        setMaxLimitInDatePicker(datePickerDialog)

        datePickerDialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun openCalenderEndDate() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val month1 = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val date11 = prefs1.getString("dateEnd", "30")!!.toInt()
        val month11 = prefs1.getString("monthEnd", "$month")!!.toInt()
        val year11 = prefs1.getString("yearEnd", "$year")!!.toInt()

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            datePickupStartWater.text = (" $dayOfMonth $month1. $year")
            billingStartDateWater.text = (" $dayOfMonth $month1. $year")
            prefs1.edit().putString("dateEnd", dayOfMonth.toString()).apply()
            prefs1.edit().putString("yearEnd", year.toString()).apply()
            prefs1.edit().putString("monthEnd", month.toString()).apply()
            initData()
            setRecyclerView()}, year11, month11, date11)

        setMinLimitInDatePicker(datePickerDialog)

        datePickerDialog.show()
    }

    private fun setMinLimitInDatePicker(datePickerDialog: DatePickerDialog) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val date11 = prefs1.getString("date", "1")!!.toInt()
        val month11 = prefs1.getString("month", "$month")!!.toInt()
        val year11 = prefs1.getString("year", "$year")!!.toInt()

        c[year11, month11] = date11
        val minDateInMilliSeconds = c.timeInMillis

        datePickerDialog.datePicker.minDate = minDateInMilliSeconds
    }

    private fun setMaxLimitInDatePicker(datePickerDialog: DatePickerDialog) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val date11 = prefs1.getString("dateEnd", "30")!!.toInt()
        val month11 = prefs1.getString("monthEnd", "$month")!!.toInt()
        val year11 = prefs1.getString("yearEnd", "$year")!!.toInt()

        c[year11, month11] = date11
        val minDateInMilliSeconds = c.timeInMillis

        datePickerDialog.datePicker.maxDate = minDateInMilliSeconds
    }


    private fun setRecyclerView() {

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val dateStart = prefs1.getString("date", "1")!!.toInt()
        val monthStart = prefs1.getString("month", "$month")!!.toInt()
        val yearStart = prefs1.getString("year", "$year")!!.toInt()
        val dateEnd = prefs1.getString("dateEnd", "30")!!.toInt()
        val monthEnd = prefs1.getString("monthEnd", "$month")!!.toInt()
        val yearEnd = prefs1.getString("yearEnd", "$year")!!.toInt()

        val dateSTART = "$yearStart-${monthStart+1}-$dateStart"
        val dateEND = "$yearEnd-${monthEnd+1}-$dateEnd"

        val userId = intent.getStringExtra("id")!!.toInt()

        list1 = ArrayList()
        list1 = DatabaseClass.getDatabase(this)!!.dao!!.getFilteredWaterData(dateSTART, dateEND,userId)

        if (list1!!.size > 0){
            recyclerViewInvoiceItems.layoutManager = LinearLayoutManager(this)
            recyclerViewInvoiceItems.setHasFixedSize(true)
            val adapter = RecyclerViewAdapterWaterInvoiceList(list1!!, this)
            recyclerViewInvoiceItems.adapter = adapter
            recyclerViewInvoiceItems.visibility = View.VISIBLE
            rvCardViewLayout.visibility =  View.VISIBLE
        }
        else
        {
            recyclerViewInvoiceItems.visibility = View.GONE
            rvCardViewLayout.visibility =  View.GONE
            amountRs.text = "₹ 0.00"
        }
    }


    override fun updateBillAmount(totalItemAmount: Double) {
        tvTotalAmount.text = "₹ " + String.format("%.2f", totalItemAmount)
        amountRs.text = "₹ " + String.format("%.2f", totalItemAmount)
    }

}
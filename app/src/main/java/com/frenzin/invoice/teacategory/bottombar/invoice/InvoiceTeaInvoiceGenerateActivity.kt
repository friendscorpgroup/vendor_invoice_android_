package com.frenzin.invoice.teacategory.bottombar.invoice

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
import com.frenzin.invoice.roomdatabase.UserDetailsModelTeaCoffee
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NAME_SHADOWING")
class InvoiceTeaInvoiceGenerateActivity : AppCompatActivity(), UpdateBillAmount{

    lateinit var recyclerViewInvoiceItems: RecyclerView
    lateinit var tvTotalAmount: TextView
    lateinit var tvCustomerName: TextView
    lateinit var tvOfficeNo: TextView
    lateinit var amountRs: TextView
    lateinit var datePickupStart: TextView
    lateinit var datePickupEnd: TextView
    lateinit var billingStartDate: TextView
    lateinit var billingEndDate: TextView
    lateinit var backBtnGenerateInvoice: ImageView
    lateinit var datePickerStartLayout: LinearLayout
    lateinit var datePickerEndLayout: LinearLayout
    lateinit var rvCardViewLayoutTea: CardView
    private var list: List<UserDetailsModelTeaCoffee>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_invoice_tea)

        setFindViewById()
        onClickListener()
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
        val dateStart = prefs1.getString("dateT", "1")!!.toInt()
        val monthStart = prefs1.getString("monthT", "$month")!!.toInt()
        val yearStart = prefs1.getString("yearT", "$year")!!.toInt()
        val dateEnd = prefs1.getString("dateEndT", "30")!!.toInt()
        val monthEnd = prefs1.getString("monthEndT", "$month")!!.toInt()
        val yearEnd = prefs1.getString("yearEndT", "$year")!!.toInt()

        val month_date = SimpleDateFormat("MMM")
        cal[Calendar.MONTH] = monthStart
        val monthStart1: String = month_date.format(cal.time)

        val month_dateEnd = SimpleDateFormat("MMM")
        cal[Calendar.MONTH] = monthEnd
        val monthEnd1: String = month_dateEnd.format(cal.time)

        val formattedDate = "$dateStart $monthStart1. $yearStart"
        val formattedDate1 = "$dateEnd $monthEnd1. $yearEnd"

        datePickupStart.text = formattedDate
        datePickupEnd.text = formattedDate1
        billingStartDate.text = formattedDate
        billingEndDate.text = formattedDate1
    }


    private fun setFindViewById() {

        recyclerViewInvoiceItems = findViewById(R.id.recyclerViewInvoiceItemsTeaCoffee)
        tvTotalAmount = findViewById(R.id.tvTotalAmountTea)
        backBtnGenerateInvoice = findViewById(R.id.backBtnGenerateInvoiceTea)
        tvCustomerName = findViewById(R.id.tvCustomerName)
        tvOfficeNo = findViewById(R.id.tvOfficeNo)
        amountRs = findViewById(R.id.amountRsT)
        datePickupStart = findViewById(R.id.datePickupStart)
        datePickerStartLayout = findViewById(R.id.datePickerStartLayout)
        datePickerEndLayout = findViewById(R.id.datePickerEndLayout)
        datePickupEnd = findViewById(R.id.datePickupEnd)
        billingEndDate = findViewById(R.id.billingEndDate)
        billingStartDate = findViewById(R.id.billingStartDate)
        rvCardViewLayoutTea = findViewById(R.id.rvCardViewLayoutTea)
    }

    private fun onClickListener() {
        backBtnGenerateInvoice.setOnClickListener(onClickListener)
        datePickerStartLayout.setOnClickListener(onClickListener)
        datePickerEndLayout.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.backBtnGenerateInvoiceTea -> {
                    finish()
                }
                R.id.datePickerStartLayout -> {
                    openCalenderStartDate()
                }
                R.id.datePickerEndLayout -> {
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
        val date11 = prefs1.getString("dateT", "1")!!.toInt()
        val month11 = prefs1.getString("monthT", "$month")!!.toInt()
        val year11 = prefs1.getString("yearT", "$year")!!.toInt()

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            datePickupStart.text = (" $dayOfMonth $month1. $year")
            billingStartDate.text = (" $dayOfMonth $month1. $year")
            prefs1.edit().putString("dateT", dayOfMonth.toString()).apply()
            prefs1.edit().putString("yearT", year.toString()).apply()
            prefs1.edit().putString("monthT", month.toString()).apply()
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
        val date11 = prefs1.getString("dateEndT", "30")!!.toInt()
        val month11 = prefs1.getString("monthEndT", "$month")!!.toInt()
        val year11 = prefs1.getString("yearEndT", "$year")!!.toInt()

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            datePickupEnd.text = (" $dayOfMonth $month1. $year")
            billingEndDate.text = (" $dayOfMonth $month1. $year")
            prefs1.edit().putString("dateEndT", dayOfMonth.toString()).apply()
            prefs1.edit().putString("yearEndT", year.toString()).apply()
            prefs1.edit().putString("monthEndT", month.toString()).apply()
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
        val date11 = prefs1.getString("dateT", "1")!!.toInt()
        val month11 = prefs1.getString("monthT", "$month")!!.toInt()
        val year11 = prefs1.getString("yearT", "$year")!!.toInt()

        c[year11, month11] = date11
        val minDateInMilliSeconds = c.timeInMillis

        datePickerDialog.datePicker.minDate = minDateInMilliSeconds
    }

    private fun setMaxLimitInDatePicker(datePickerDialog: DatePickerDialog) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val date11 = prefs1.getString("dateEndT", "30")!!.toInt()
        val month11 = prefs1.getString("monthEndT", "$month")!!.toInt()
        val year11 = prefs1.getString("yearEndT", "$year")!!.toInt()

        c[year11, month11] = date11
        val minDateInMilliSeconds = c.timeInMillis

        datePickerDialog.datePicker.maxDate = minDateInMilliSeconds
    }


    override fun onResume() {
        super.onResume()
        initData()
        setRecyclerView()
    }

    private fun setRecyclerView() {

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val dateStart = prefs1.getString("dateT", "1")!!.toInt()
        val monthStart = prefs1.getString("monthT", "$month")!!.toInt()
        val yearStart = prefs1.getString("yearT", "$year")!!.toInt()
        val dateEnd = prefs1.getString("dateEndT", "30")!!.toInt()
        val monthEnd = prefs1.getString("monthEndT", "$month")!!.toInt()
        val yearEnd = prefs1.getString("yearEndT", "$year")!!.toInt()

        val dateSTART = "$yearStart-${monthStart+1}-$dateStart"
        val dateEND = "$yearEnd-${monthEnd+1}-$dateEnd"


        val userId = intent.getStringExtra("id")!!.toInt()

        list = ArrayList()
        list = DatabaseClass.getDatabase(this)!!.dao!!.getFilteredTeaData(dateSTART, dateEND, userId)

        if (list!!.isNotEmpty()){
            recyclerViewInvoiceItems.layoutManager = LinearLayoutManager(this)
            recyclerViewInvoiceItems.setHasFixedSize(true)
            val adapter = RecyclerViewAdapterTeaCoffeeInvoiceList(list!!, this)
            recyclerViewInvoiceItems.adapter = adapter
            recyclerViewInvoiceItems.visibility = View.VISIBLE
            rvCardViewLayoutTea.visibility =  View.VISIBLE
        }
        else
        {
            recyclerViewInvoiceItems.visibility = View.GONE
            rvCardViewLayoutTea.visibility =  View.GONE
            amountRs.text = "₹ 0.00"
        }
    }

    override fun updateBillAmount(totalItemAmount: Double) {
        tvTotalAmount.text = "₹ " + String.format("%.2f", totalItemAmount)
        amountRs.text = "₹ " + String.format("%.2f", totalItemAmount)
    }
}
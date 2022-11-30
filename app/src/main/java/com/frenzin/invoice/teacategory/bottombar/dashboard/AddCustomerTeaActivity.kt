package com.frenzin.invoice.teacategory.bottombar.dashboard

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.frenzin.invoice.R
import com.frenzin.invoice.roomdatabase.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddCustomerTeaActivity : AppCompatActivity() {

    companion object{
        const val TAG = "ERRORTAG##DEBUGGING"
    }
    lateinit var btnSubmit : MaterialButton
    lateinit var backBtn : ImageView
    lateinit var fName : EditText
    lateinit var lName : EditText
    lateinit var officeName : EditText
    lateinit var phoneNumber : EditText
    lateinit var address : EditText
    lateinit var coffeePrice : EditText
    lateinit var teaPrice : EditText
    lateinit var mrngTime : EditText
    lateinit var evngTime : EditText
    lateinit var evngTimePicker : MaterialCardView
    lateinit var mrngTimePicker : MaterialCardView
    private var millis: Long ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer_tea)

        setFindViewById()
        onClickListener()

        if (intent.getStringExtra("key").equals("EditCustomer")){
            setData()
        }
    }


    private fun setData() {
        fName.setText(intent.getStringExtra("fname"))
        lName.setText(intent.getStringExtra("lname"))
        officeName.setText(intent.getStringExtra("officename"))
        phoneNumber.setText(intent.getStringExtra("phonenumber"))
        address.setText(intent.getStringExtra("address"))
        coffeePrice.setText(intent.getStringExtra("coffeprice"))
        teaPrice.setText(intent.getStringExtra("teaprice"))
        mrngTime.setText(intent.getStringExtra("morningtime"))
        evngTime.setText(intent.getStringExtra("eveningtime"))
    }


    private fun setFindViewById() {
        btnSubmit = findViewById(R.id.btnSubmitTea)
        backBtn = findViewById(R.id.backBtnAddCustomerTea)
        fName = findViewById(R.id.fName)
        lName = findViewById(R.id.lName)
        officeName = findViewById(R.id.officeName)
        phoneNumber = findViewById(R.id.phoneNumber)
        address = findViewById(R.id.address)
        coffeePrice = findViewById(R.id.etCoffeePrice)
        teaPrice = findViewById(R.id.etTeaPrice)
        mrngTime = findViewById(R.id.mrngTime)
        evngTime = findViewById(R.id.evngTime)
        evngTimePicker = findViewById(R.id.evngTimePicker)
        mrngTimePicker = findViewById(R.id.mrngTimePicker)
    }

    private fun onClickListener() {
        btnSubmit.setOnClickListener(onClickListener)
        backBtn.setOnClickListener(onClickListener)
        evngTimePicker.setOnClickListener(onClickListener)
        mrngTimePicker.setOnClickListener(onClickListener)
        mrngTime.setOnClickListener(onClickListener)
        evngTime.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.btnSubmitTea -> {
                    if (checkAllFields()){
                        if (intent.getStringExtra("key").equals("EditCustomer")){
                            updateCustomerDataToDB()
                        }
                        else if(intent.getStringExtra("key").equals("AddCustomerData"))
                        {
                            addCustomerToDB()
                        }
                    }
                    else {
                        return
                    }
                }
                R.id.backBtnAddCustomerTea -> {
                    finish()
                }
                R.id.mrngTimePicker -> {
                    morningTimePicker()
                }
                R.id.evngTimePicker -> {
                    eveningTimePicker()
                }
                R.id.mrngTime -> {
                    morningTimePicker()
                }
                R.id.evngTime -> {
                    eveningTimePicker()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun morningTimePicker() {

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val hour1 = prefs1.getString("hour", "$hour")!!.toInt()
        val minute1 = prefs1.getString("minute", "$minute")!!.toInt()

        val timeFormat = SimpleDateFormat("KK:mm a")

        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->

                c[Calendar.HOUR_OF_DAY] = hour1
                c[Calendar.MINUTE] = minute1

                val finalTime: String = timeFormat.format(c.time)

                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                c.set(Calendar.SECOND, 0)
                c.set(Calendar.MILLISECOND, 0)

                millis =  c.timeInMillis

                if (hourOfDay > 13){
                    showAlertForTimePicker()
                    return@TimePickerDialog
                }
                else if (hourOfDay < 13)
                {
                    mrngTime.setText(finalTime)
                }

                prefs1.edit().putString("hour", hourOfDay.toString()).apply()
                prefs1.edit().putString("minute", minute.toString()).apply()
                initMorningTimeData()
            }, hour1, minute1, false)

        timePickerDialog.show()
    }

    private fun showAlertForTimePicker() {

        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Please select time before 1 pm")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Okay"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            morningTimePicker()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun eveningTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val hour1 = prefs1.getString("hourEve", "$hour")!!.toInt()
        val minute1 = prefs1.getString("minuteEve", "$minute")!!.toInt()

        val timeFormat = SimpleDateFormat("KK:mm a")

        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                c[Calendar.HOUR_OF_DAY] = hour1
                c[Calendar.MINUTE] = minute1
                val finalTime: String = timeFormat.format(c.time)

                if (hourOfDay < 13){
                    showAlertForTimePickerEvening()
                    return@TimePickerDialog
                }
                else if (hourOfDay > 13)
                {
                    evngTime.setText(finalTime)
                }

                prefs1.edit().putString("hourEve", hourOfDay.toString()).apply()
                prefs1.edit().putString("minuteEve", minute.toString()).apply()
                initEveningTimeData()
            }, hour1, minute1, false)

        timePickerDialog.show()
    }

    private fun showAlertForTimePickerEvening() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Please select time after 1 pm")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Okay"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            eveningTimePicker()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun initEveningTimeData(){
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val hourEve = prefs1.getString("hourEve", "$hour")!!.toInt()
        val minuteEve = prefs1.getString("minuteEve", "$minute")!!.toInt()

        val timeFormatEve = SimpleDateFormat("kk:mm")
        c[Calendar.HOUR_OF_DAY] = hourEve
        c[Calendar.MINUTE] = minuteEve
        val finalTimeEve: String = timeFormatEve.format(c.time)

        evngTime.setText(finalTimeEve)

        prefs1.edit().putString("evngTime", finalTimeEve).apply()

    }

    private fun initMorningTimeData() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val hour1 = prefs1.getString("hour", "$hour")!!.toInt()
        val minute1 = prefs1.getString("minute", "$minute")!!.toInt()

        val timeFormat = SimpleDateFormat("kk:mm")
        c[Calendar.HOUR_OF_DAY] = hour1
        c[Calendar.MINUTE] = minute1
        val finalTime: String = timeFormat.format(c.time)

        mrngTime.setText(finalTime)
        prefs1.edit().putString("mrngTime", finalTime).apply()

    }

    private fun updateCustomerDataToDB() {

        val id = intent.getStringExtra("userid")!!.toInt()
        val fname = fName.text.toString()
        val lname = lName.text.toString()
        val officename = officeName.text.toString()
        val phonenumber = phoneNumber.text.toString()
        val address1 = address.text.toString()
        val teaprice = teaPrice.text.toString()
        val coffeprice = coffeePrice.text.toString()
        val morningTime = mrngTime.text.toString()
        val eveningTime = evngTime.text.toString()

        DatabaseClass.getDatabase(applicationContext)!!.dao!!.updateCustomerData(fname, lname, officename,
            phonenumber, address1, morningTime, eveningTime, teaprice, coffeprice, null, null, id)


        showAlertDialog()

        fName.setText("")
        lName.setText("")
        officeName.setText("")
        phoneNumber.setText("")
        address.setText("")
        teaPrice.setText("")
        coffeePrice.setText("")
        mrngTime.setText("")
        evngTime.setText("")
    }

    private fun checkAllFields() : Boolean{
        if (fName.text.toString().isEmpty()) {
            fName.error = "Please enter first name"
            fName.requestFocus()
            return false
        }

        if (officeName.text.toString().isEmpty()) {
            officeName.error = "Please enter office name"
            officeName.requestFocus()
            return false
        }

        if (phoneNumber.text.toString().isEmpty()) {
            phoneNumber.error = "Please enter mobile number"
            phoneNumber.requestFocus()
            return false
        }

        if (phoneNumber.text.length < 10) {
            phoneNumber.error = "Please enter 10 digit mobile number"
            phoneNumber.requestFocus()
            return false
        }

        if (phoneNumber.text.length > 10) {
            phoneNumber.error = "Please enter 10 digit mobile number"
            phoneNumber.requestFocus()
            return false
        }

        if (address.text.toString().isEmpty()) {
            address.error = "Please enter address"
            address.requestFocus()
            return false
        }

        if (mrngTime.text.toString().isEmpty()){
            mrngTime.error = "Select morning time"
            mrngTime.requestFocus()
            return false
        }

        if (evngTime.text.toString().isEmpty()){
            evngTime.error = "Select evening time"
            evngTime.requestFocus()
            return false
        }

        if (teaPrice.text.toString().isEmpty()) {
            teaPrice.error = "Please enter tea price"
            teaPrice.requestFocus()
            return false
        }

        if (coffeePrice.text.toString().isEmpty()) {
            coffeePrice.error = "Please enter coffee price"
            coffeePrice.requestFocus()
            return false
        }


        return true
    }

    private fun addCustomerToDB() {

        val fName1: String = fName.text.toString()
        val lName1: String = lName.text.toString()
        val officeName1: String = officeName.text.toString()
        val phoneNumber1: String = phoneNumber.text.toString()
        val address1: String = address.text.toString()
        val teaPrice1: String = teaPrice.text.toString()
        val coffeePrice1: String = coffeePrice.text.toString()

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        val prefs1: SharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val morningtime = prefs1.getString("mrngTime", "Morning time")
        val eveningtime = prefs1.getString("evngTime", "Evening time")

        val model = UserModel()
        model.createdDate = formattedDate
        model.customerType = "TEA"
        model.fname = fName1
        model.lname = lName1
        model.officename = officeName1
        model.phonenumber = phoneNumber1
        model.address = address1
        model.morningtime = morningtime
        model.eveningtime = eveningtime
        model.teaprice = teaPrice1
        model.coffeprice = coffeePrice1
        model.waterprice = null

        DatabaseClass.getDatabase(applicationContext)!!.dao!!.insertAllData(model)

        showAlertDialog()

        fName.setText("")
        lName.setText("")
        officeName.setText("")
        phoneNumber.setText("")
        address.setText("")
        teaPrice.setText("")
        coffeePrice.setText("")
        mrngTime.setText("")
        evngTime.setText("")

    }

    @SuppressLint("MissingInflatedId")
    private fun showAlertDialog() {

        val builder = MaterialAlertDialogBuilder(this@AddCustomerTeaActivity, R.style.RoundedCornersDialog)
        val customLayout: View = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(customLayout)
        builder.setCancelable(false)

        val btnDone: MaterialButton = customLayout.findViewById(R.id.btnDone)
        val btnAddMore: MaterialButton = customLayout.findViewById(R.id.btnAddMore)
        val tvAdded: TextView = customLayout.findViewById(R.id.tvAdded)

        if (intent.getStringExtra("key").equals("EditCustomer")){
            tvAdded.setText(R.string.customer_updated_successfully)
            btnAddMore.visibility = View.GONE
        }
        else if(intent.getStringExtra("key").equals("AddCustomerData"))
        {
            btnAddMore.visibility = View.VISIBLE
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

        btnDone.setOnClickListener {
            dialog.dismiss()
            this.finish()
        }

        btnAddMore.setOnClickListener {
            dialog.dismiss()
        }
    }
}


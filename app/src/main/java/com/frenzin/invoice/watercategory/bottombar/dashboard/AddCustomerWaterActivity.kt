package com.frenzin.invoice.watercategory.bottombar.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.frenzin.invoice.R
import com.frenzin.invoice.roomdatabase.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class AddCustomerWaterActivity : AppCompatActivity() {

    lateinit var btnSubmit : MaterialButton
    lateinit var backBtn : ImageView
    lateinit var fName : EditText
    lateinit var lName : EditText
    lateinit var officeName : EditText
    lateinit var phoneNumber : EditText
    lateinit var address : EditText
    lateinit var autocompleteTV : AutoCompleteTextView
    lateinit var priceAmount : EditText
    lateinit var types : Array<String>
    lateinit var arrayAdapter : ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer_water)

        setFindViewById()
        onClickListener()
        setDropDownAdapter()

        if (intent.getStringExtra("key").equals("EditCustomerWater")){
            initData()
        }

    }

    private fun initData() {
        fName.setText(intent.getStringExtra("fname"))
        lName.setText(intent.getStringExtra("lname"))
        officeName.setText(intent.getStringExtra("officename"))
        phoneNumber.setText(intent.getStringExtra("phonenumber"))
        address.setText(intent.getStringExtra("address"))
        priceAmount.setText(intent.getStringExtra("waterprice"))
        autocompleteTV.setText(intent.getStringExtra("type"))
    }

    private fun setDropDownAdapter() {
        types = resources.getStringArray(R.array.watertype)
        arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, types)
        autocompleteTV = findViewById(R.id.autoCompleteTextView)
        autocompleteTV.setAdapter(arrayAdapter)
    }

    private fun setFindViewById() {
        btnSubmit = findViewById(R.id.btnSubmit)
        backBtn = findViewById(R.id.backBtnAddCustomer)
        fName = findViewById(R.id.fNameW)
        lName = findViewById(R.id.lNameW)
        officeName = findViewById(R.id.officeNameW)
        phoneNumber = findViewById(R.id.phoneNumberW)
        address = findViewById(R.id.addressW)
        priceAmount = findViewById(R.id.priceAmountW)
    }

    private fun onClickListener() {
        btnSubmit.setOnClickListener(onClickListener)
        backBtn.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.btnSubmit -> {
                    if (checkAllFields()){
                        if (intent.getStringExtra("key").equals("EditCustomerWater")){
                            updateCustomerDataToDB()
                        }
                        else if(intent.getStringExtra("key").equals("AddCustomerDataWater"))
                        {
                            addCustomerToDB()
                        }
                    }
                    else
                    {
                        return
                    }
                }
                R.id.backBtnAddCustomer -> {
                    finish()
                }
            }
        }
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


        if (priceAmount.text.toString().isEmpty()) {
            priceAmount.error = "Please enter price"
            priceAmount.requestFocus()
            return false
        }

        return true
    }

    private fun updateCustomerDataToDB() {

        val id = intent.getStringExtra("userid")!!.toInt()
        val fname = fName.text.toString()
        val lname = lName.text.toString()
        val officename = officeName.text.toString()
        val phonenumber = phoneNumber.text.toString()
        val address1 = address.text.toString()
        val type1: String = autocompleteTV.text.toString()
        val priceAmount1: String = priceAmount.text.toString()

        DatabaseClass.getDatabase(applicationContext)!!.dao!!.updateCustomerData(fname, lname, officename,
            phonenumber, address1, null, null,null, null, priceAmount1, type1, id)

        showAlertDialog()

        fName.setText("")
        lName.setText("")
        officeName.setText("")
        phoneNumber.setText("")
        address.setText("")
        priceAmount.setText("")
    }


    private fun addCustomerToDB() {

        val fName1: String = fName.text.toString()
        val lName1: String = lName.text.toString()
        val officeName1: String = officeName.text.toString()
        val phoneNumber1: String = phoneNumber.text.toString()
        val address1: String = address.text.toString()
        val type1: String = autocompleteTV.text.toString()
        val priceAmount1: String = priceAmount.text.toString()

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)

        if (autocompleteTV.text.toString().equals("Select Type")) {
            Toast.makeText(this, "Please select type", Toast.LENGTH_SHORT).show()
            return
        }
        else
        {
            val model = UserModel()
            model.createdDate = formattedDate
            model.customerType = "WATER"
            model.fname = fName1
            model.lname = lName1
            model.officename = officeName1
            model.phonenumber = phoneNumber1
            model.address = address1
            model.type = type1
            model.teaprice = null
            model.coffeprice = null
            model.waterprice = priceAmount1

            DatabaseClass.getDatabase(applicationContext)!!.dao!!.insertAllData(model)
            showAlertDialog()

            fName.setText("")
            lName.setText("")
            officeName.setText("")
            phoneNumber.setText("")
            address.setText("")
            priceAmount.setText("")
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun showAlertDialog() {

        val builder = MaterialAlertDialogBuilder(this@AddCustomerWaterActivity, R.style.RoundedCornersDialog)
        val customLayout: View = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(customLayout)
        builder.setCancelable(false)

        val btnDone: MaterialButton = customLayout.findViewById(R.id.btnDone)
        val btnAddMore: MaterialButton = customLayout.findViewById(R.id.btnAddMore)
        val tvAdded: TextView = customLayout.findViewById(R.id.tvAdded)

        if (intent.getStringExtra("key").equals("EditCustomerWater")){
            tvAdded.setText(R.string.customer_updated_successfully)
            btnAddMore.visibility = View.GONE
        }
        else if(intent.getStringExtra("key").equals("AddCustomerDataWater"))
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

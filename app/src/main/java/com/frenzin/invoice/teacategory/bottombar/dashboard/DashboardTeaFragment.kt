package com.frenzin.invoice.teacategory.bottombar.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.frenzin.invoice.R
import com.frenzin.invoice.databinding.FragmentDashboardTeaBinding
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserDetailsModelTeaCoffee
import com.frenzin.invoice.roomdatabase.UserModel
import com.frenzin.invoice.teacategory.bottombar.dashboard.coffee.CoffeeSectionFragment
import com.frenzin.invoice.teacategory.bottombar.dashboard.tea.TeaSectionFragment
import com.frenzin.invoice.watercategory.bottombar.dashboard.OpenAlertDialog
import com.frenzin.invoice.watercategory.bottombar.dashboard.ViewPagerAdapterWater
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class DashboardTeaFragment : Fragment(), OpenAlertDialog , DeleteCustomer{

    private var _binding: FragmentDashboardTeaBinding? = null
    private val binding get() = _binding!!
    private var list: List<UserModel>? = null
    private var list1: List<UserDetailsModelTeaCoffee>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardTeaBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        onClickListener()
        setUpTabLayout()
        setupViewPager(binding.viewPagerTea)
        setRecyclerView()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }


    private fun onClickListener() {
        binding.addCustomerTea.setOnClickListener(onClickListener)
        binding.tvAddCustomerTea.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.addCustomerTea -> {
                    val intent = Intent(requireActivity(), AddCustomerTeaActivity::class.java)
                    intent.putExtra("key", "AddCustomerData")
                    startActivity(intent)
                }
                R.id.tvAddCustomerTea -> {
                    val intent = Intent(requireActivity(), AddCustomerTeaActivity::class.java)
                    intent.putExtra("key", "AddCustomerData")
                    startActivity(intent)
                }
            }
        }
    }

    private fun setUpTabLayout() {
        binding.tabLayoutTea.setupWithViewPager(binding.viewPagerTea)
    }

    private fun setupViewPager(viewpager: ViewPager) {

        val adapter = ViewPagerAdapterWater(childFragmentManager)
        adapter.addFragment(TeaSectionFragment(), "Tea")
        adapter.addFragment(CoffeeSectionFragment(), "Coffee")
        viewpager.adapter = adapter
    }

    private fun setRecyclerView() {

        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]

        list = ArrayList()

        if (hour < 13){
            list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataAccMRNGtime("TEA")
        }
        else if (hour > 13){
            list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataAccEVNGtime("TEA")
        }

        binding.recyclerViewCustomerListTea.layoutManager = LinearLayoutManager(requireContext())

        if (list!!.isNotEmpty()) {
            binding.recyclerViewCustomerListTea.setHasFixedSize(true)
            val adapter = RecyclerViewAdapterTeaCustomerList(list!!, this, this)
            binding.recyclerViewCustomerListTea.adapter = adapter
            binding.tvCustomerListTea.visibility = View.VISIBLE
            binding.recyclerViewCustomerListTea.visibility = View.VISIBLE
            binding.tvAddCustomerTea.visibility = View.GONE
        }
        else{
            showMessage1()
        }

    }

    fun hideData() {
        binding.tvCustomerListTea.visibility = View.GONE
        binding.recyclerViewCustomerListTea.visibility = View.GONE
        binding.tvAddCustomerTea.visibility = View.VISIBLE
    }

    override fun openAlertDialog(
        id: String,
        fname: String?,
        teaItemMrng: Int,
        coffeeItemMrng: Int,
        teaItemEvng: Int,
        coffeeItemEvng: Int
    ) {
        showAlertDialog(id, fname, teaItemMrng, coffeeItemMrng, teaItemEvng, coffeeItemEvng)
    }

    override fun showMessage() {
        TODO("Not yet implemented")
    }

    private fun showMessage1() {
        hideData()
    }

    private fun showAlertDialog(
        id: String,
        fname: String?,
        teaItemMrng: Int?,
        coffeeItemMrng: Int?,
        teaItemEvng: Int,
        coffeeItemEvng: Int
    ) {
        val dialog = Dialog(requireContext(), R.style.RoundedCornersDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert_dialog_add_tea)

        val saveBtn: MaterialButton = dialog.findViewById(R.id.saveBtnTea)
        val btnRedCross: ImageView = dialog.findViewById(R.id.btnRedCross)
        val btnIncreaseTeaMrng: ImageButton = dialog.findViewById(R.id.btnIncreaseTeaMrng)
        val btnDecreaseTeaMrng: ImageButton = dialog.findViewById(R.id.btnDecreaseTeaMrng)
        val tvTeaCountMrng: TextView = dialog.findViewById(R.id.tvTeaCountMrng)
        val btnIncreaseCoffeeMrng: ImageButton = dialog.findViewById(R.id.btnIncreaseCoffeeMrng)
        val btnDecreaseCoffeeMrng: ImageButton = dialog.findViewById(R.id.btnDecreaseCoffeeMrng)
        val tvCoffeeCountMrng: TextView = dialog.findViewById(R.id.tvCoffeeCountMrng)

        val btnIncreaseTeaEve: ImageButton = dialog.findViewById(R.id.btnIncreaseTeaEve)
        val btnDecreaseTeaEve: ImageButton = dialog.findViewById(R.id.btnDecreaseTeaEve)
        val tvTeaCountEve: TextView = dialog.findViewById(R.id.tvTeaCountEve)
        val btnIncreaseCoffeeEve: ImageButton = dialog.findViewById(R.id.btnIncreaseCoffeeEve)
        val btnDecreaseCoffeeEve: ImageButton = dialog.findViewById(R.id.btnDecreaseCoffeeEve)
        val tvCoffeeCountEve: TextView = dialog.findViewById(R.id.tvCoffeeCountEve)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        var teaItemNewMrng = teaItemMrng!!.toInt()
        var coffeeItemNewMrng = coffeeItemMrng!!.toInt()
        var teaItemNewEvng = teaItemEvng
        var coffeeItemNewEvng = coffeeItemEvng

        tvTeaCountMrng.text = teaItemMrng.toString()
        tvCoffeeCountMrng.text = coffeeItemMrng.toString()
        tvTeaCountEve.text = teaItemEvng.toString()
        tvCoffeeCountEve.text = coffeeItemEvng.toString()

        btnRedCross.setOnClickListener {
            dialog.dismiss()
        }

        btnIncreaseTeaMrng.setOnClickListener {
            teaItemNewMrng++
            tvTeaCountMrng.text = teaItemNewMrng.toString()
        }

        btnDecreaseTeaMrng.setOnClickListener {
            teaItemNewMrng -= if (teaItemNewMrng > 0) 1 else 0
            tvTeaCountMrng.text = teaItemNewMrng.toString()
        }

        btnIncreaseCoffeeMrng.setOnClickListener {
            coffeeItemNewMrng++
            tvCoffeeCountMrng.text = coffeeItemNewMrng.toString()
        }

        btnDecreaseCoffeeMrng.setOnClickListener {
            coffeeItemNewMrng -= if (coffeeItemNewMrng > 0) 1 else 0
            tvCoffeeCountMrng.text = coffeeItemNewMrng.toString()
        }

        btnIncreaseTeaEve.setOnClickListener {
            teaItemNewEvng++
            tvTeaCountEve.text = teaItemNewEvng.toString()
        }

        btnDecreaseTeaEve.setOnClickListener {
            teaItemNewEvng -= if (teaItemNewEvng > 0) 1 else 0
            tvTeaCountEve.text = teaItemNewEvng.toString()
        }

        btnIncreaseCoffeeEve.setOnClickListener {
            coffeeItemNewEvng++
            tvCoffeeCountEve.text = coffeeItemNewEvng.toString()
        }

        btnDecreaseCoffeeEve.setOnClickListener {
            coffeeItemNewEvng -= if (coffeeItemNewEvng > 0) 1 else 0
            tvCoffeeCountEve.text = coffeeItemNewEvng.toString()
        }


        saveBtn.setOnClickListener {

            val c: Date = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate: String = df.format(c)

            list1 = ArrayList()
            list1 = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataCreatedDate(formattedDate,id.toInt())


            if (list1!!.isNotEmpty())
            {
                DatabaseClass.getDatabase(requireContext())!!.dao!!.updateData(
                    teaItemNewMrng.toString(),
                    coffeeItemNewMrng.toString(),
                    teaItemNewEvng.toString(),
                    coffeeItemNewEvng.toString(),
                    list1!![0].key)
            }
            else
            {
                val model = UserDetailsModelTeaCoffee()
                model.userId = id
                model.fname = fname
                model.createdDate = formattedDate
                model.teaItemMrng = teaItemNewMrng.toString()
                model.coffeeItemMrng = coffeeItemNewMrng.toString()
                model.teaItemEvng = teaItemNewEvng.toString()
                model.coffeeItemEvng = coffeeItemNewEvng.toString()

                DatabaseClass.getDatabase(requireContext())!!.dao!!.insertDetailsTea(model)
            }

            setUpTabLayout()
            setupViewPager(binding.viewPagerTea)
            setRecyclerView()

            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun deleteCustomer(id: String?) {
        openAlertDialog1(id)
    }

    private fun openAlertDialog1(id1: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert!")
            .setMessage("Are you sure you want to delete customer?")
            .setCancelable(false)
            .setPositiveButton("Yes"
            ) { dialog, id -> deleteData(id1.toString()) }
            .setNegativeButton("No"
            ) { dialog, id -> dialog.cancel() }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun deleteData(id: String?) {
        DatabaseClass.getDatabase(requireContext())!!.dao!!.deleteData(id!!.toInt())
        setRecyclerView()
    }
}
package com.frenzin.invoice.watercategory.bottombar.dashboard

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
import com.frenzin.invoice.databinding.FragmentDashboardWaterBinding
import com.frenzin.invoice.roomdatabase.*
import com.frenzin.invoice.teacategory.bottombar.dashboard.DeleteCustomer
import com.frenzin.invoice.watercategory.bottombar.dashboard.bottle.BottleSectionFragment
import com.frenzin.invoice.watercategory.bottombar.dashboard.jug.JugSectionFragment
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*


class DashboardWaterFragment : Fragment() , OpenAlertDialog, DeleteCustomer{
    private var _binding: FragmentDashboardWaterBinding? = null
    private val binding get() = _binding!!
    private var list: List<UserModel>? = null
    private var list1: List<UserDetailsModelWater>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardWaterBinding.inflate(inflater, container, false)
        container!!.removeAllViews()


        onClickListener()
        setUpTabLayout()
        setupViewPager(binding.viewPagerWater)
        setRecyclerView()

        return binding.root
    }

    private fun onClickListener() {
        binding.addCustomer.setOnClickListener(onClickListener)
        binding.tvAddCustomerWater.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.addCustomer -> {
                    val intent = Intent(requireActivity(), AddCustomerWaterActivity::class.java)
                    intent.putExtra("key","AddCustomerDataWater")
                    startActivity(intent)
                }
                R.id.tvAddCustomerWater -> {
                    val intent = Intent(requireActivity(), AddCustomerWaterActivity::class.java)
                    intent.putExtra("key","AddCustomerDataWater")
                    startActivity(intent)
                }
            }
        }
    }



    private fun setUpTabLayout() {
        binding.tabLayoutWater.setupWithViewPager(binding.viewPagerWater)
    }

    private fun setupViewPager(viewpager: ViewPager) {

        val adapter = ViewPagerAdapterWater(childFragmentManager)
        adapter.addFragment(BottleSectionFragment(), "Bottle")
        adapter.addFragment(JugSectionFragment(), "Jug")
        viewpager.adapter = adapter
    }


    private fun setRecyclerView() {
        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataFromType("WATER")
        binding.recyclerViewCustomerListWater.layoutManager = LinearLayoutManager(requireContext())

        if (list!!.isNotEmpty()) {
            binding.recyclerViewCustomerListWater.setHasFixedSize(true)
            val adapter = RecyclerViewAdapterBottleCustomerList(list!!, this, this)
            binding.recyclerViewCustomerListWater.adapter = adapter
            binding.tvCustomerListWater.visibility = View.VISIBLE
            binding.recyclerViewCustomerListWater.visibility = View.VISIBLE
            binding.tvAddCustomerWater.visibility = View.GONE
        }
        else{
            showMessage1()
        }

    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun openAlertDialog(
        id: String,
        fname: String?,
        addItem: Int,
        emptyItem: Int,
        toInt: Int,
        toInt1: Int
    ) {
        showAlertDialog(id, fname, addItem, emptyItem)
    }

    override fun showMessage() {
        TODO("Not yet implemented")
    }

    private fun showMessage1() {
        hideData()
    }

    private fun showAlertDialog(id: String, fname: String?, addItem: Int, emptyItem: Int) {
        val dialog = Dialog(requireContext(), R.style.RoundedCornersDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert_dialog_add_bottles)

        val saveBtn: MaterialButton = dialog.findViewById(R.id.saveBtn)
        val btnRedCross: ImageView = dialog.findViewById(R.id.btnRedCrossWater)
        val btnIncreaseEmpty: ImageButton = dialog.findViewById(R.id.btnIncreaseEmptyBottle)
        val btnDecreaseEmpty: ImageButton = dialog.findViewById(R.id.btnDecreaseEmptyBottle)
        val btnIncreaseAdd: ImageButton = dialog.findViewById(R.id.btnIncreaseAddBottle)
        val btnDecreaseAdd: ImageButton = dialog.findViewById(R.id.btnDecreaseAddBottle)
        val tvCountAdd: TextView = dialog.findViewById(R.id.tvCountAdd)
        val tvCountEmpty: TextView = dialog.findViewById(R.id.tvCountEmpty)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        var addItemNew = addItem
        var emptyItemNew = emptyItem

        tvCountAdd.text = addItemNew.toString()
        tvCountEmpty.text = emptyItemNew.toString()

        saveBtn.setOnClickListener {

            val c: Date = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate: String = df.format(c)

            list1 = ArrayList()
            list1 = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataWaterCreatedDate(formattedDate,id.toInt())

            if (list1!!.isNotEmpty())
            {
                DatabaseClass.getDatabase(requireContext())!!.dao!!.updateDataWater(
//                    formattedDate,
                    addItemNew.toString(),
                    emptyItemNew.toString(),
                    list1!![0].key)
            }
            else
            {
                val model = UserDetailsModelWater()
                model.userId = id
                model.fname = fname
                model.createdDate = formattedDate
                model.addbottle = addItemNew.toString()
                model.emptybottle = emptyItemNew.toString()

                DatabaseClass.getDatabase(requireContext())!!.dao!!.insertDetailsWater(model)
            }

            setUpTabLayout()
            setupViewPager(binding.viewPagerWater)
            setRecyclerView()

            dialog.dismiss()

        }

        btnRedCross.setOnClickListener {
            dialog.dismiss()
        }

        btnIncreaseEmpty.setOnClickListener {
            emptyItemNew++
            tvCountEmpty.text = emptyItemNew.toString()
        }
        btnDecreaseEmpty.setOnClickListener {
            emptyItemNew -= if (emptyItemNew > 0) 1 else 0
            tvCountEmpty.text = emptyItemNew.toString()
        }

        btnIncreaseAdd.setOnClickListener {
            addItemNew++
            tvCountAdd.text = addItemNew.toString()
        }
        btnDecreaseAdd.setOnClickListener {
            addItemNew -= if (addItemNew > 0) 1 else 0
            tvCountAdd.text = addItemNew.toString()
        }
    }

     fun hideData() {
        binding.tvCustomerListWater.visibility = View.GONE
        binding.recyclerViewCustomerListWater.visibility = View.GONE
        binding.tvAddCustomerWater.visibility = View.VISIBLE
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

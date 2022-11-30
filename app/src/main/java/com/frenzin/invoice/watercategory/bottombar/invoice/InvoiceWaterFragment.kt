package com.frenzin.invoice.watercategory.bottombar.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.frenzin.invoice.databinding.FragmentInvoiceWaterBinding
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserModel
import java.util.ArrayList

class InvoiceWaterFragment : Fragment() {

    private var _binding: FragmentInvoiceWaterBinding? = null
    private val binding get() = _binding!!
    private var list: List<UserModel>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInvoiceWaterBinding.inflate(inflater, container, false)
        container!!.removeAllViews()


        setRecyclerView()
        onClickListener()

        return binding.root
    }

    private fun onClickListener() {
//        binding.BtnGenerateInvoice.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
//                R.id.BtnGenerateInvoice -> {
//                    val intent = Intent(requireActivity(), InvoiceWaterInvoiceGenerateActivity::class.java)
//                    startActivity(intent)
//                }
            }
        }
    }

    private fun setRecyclerView() {
        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataFromType("WATER")
        binding.recyclerviewInvoiceCustomerList.layoutManager = LinearLayoutManager(requireContext())

        if (list!!.isNotEmpty()) {
            binding.recyclerviewInvoiceCustomerList.setHasFixedSize(true)
            val adapter = RecyclerViewAdapterInvoiceCustomerListWater(list!!)
            binding.recyclerviewInvoiceCustomerList.adapter = adapter
            binding.recyclerviewInvoiceCustomerList.visibility = View.VISIBLE
            binding.tvInvoiceAddCustomer.visibility = View.GONE
        }
        else
        {
            showMessage()
        }

    }

    fun showMessage() {
        binding.recyclerviewInvoiceCustomerList.visibility = View.GONE
        binding.tvInvoiceAddCustomer.visibility = View.VISIBLE
    }
}
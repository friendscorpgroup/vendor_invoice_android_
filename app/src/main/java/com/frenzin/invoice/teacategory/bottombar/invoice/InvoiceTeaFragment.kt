package com.frenzin.invoice.teacategory.bottombar.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.frenzin.invoice.databinding.FragmentInvoiceTeaBinding
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserModel

import com.frenzin.invoice.watercategory.bottombar.invoice.RecyclerViewAdapterInvoiceCustomerListWater


class InvoiceTeaFragment : Fragment() {

    private var _binding: FragmentInvoiceTeaBinding? = null
    private val binding get() = _binding!!

    private var list: List<UserModel>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInvoiceTeaBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        setRecyclerView()
        onClickListener()

        return binding.root
    }


    private fun onClickListener() {
//        binding.backBtnInvoiceT.setOnClickListener(onClickListener)

    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
//                R.id.backBtnInvoiceT -> {
//                    requireActivity().finish()
//                }
            }
        }
    }

    private fun setRecyclerView() {
        list = ArrayList()
        list = DatabaseClass.getDatabase(requireContext())!!.dao!!.getCustomerDataFromType("TEA")
        binding.recyclerviewInvoiceCustomerListTea.layoutManager = LinearLayoutManager(requireContext())

        if (list!!.isNotEmpty()) {
            binding.recyclerviewInvoiceCustomerListTea.setHasFixedSize(true)
            val adapter = RecyclerViewAdapterInvoiceCustomerTeaCoffee(list!!)
            binding.recyclerviewInvoiceCustomerListTea.adapter = adapter
            binding.recyclerviewInvoiceCustomerListTea.visibility = View.VISIBLE
            binding.tvInvoiceAddCustomerTea.visibility = View.GONE
        }
        else{
            showMessage1()
        }

    }


    fun showMessage1() {
        binding.recyclerviewInvoiceCustomerListTea.visibility = View.GONE
        binding.tvInvoiceAddCustomerTea.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

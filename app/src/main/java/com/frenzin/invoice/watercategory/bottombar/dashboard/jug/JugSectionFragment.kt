package com.frenzin.invoice.watercategory.bottombar.dashboard.jug

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.frenzin.invoice.R
import com.frenzin.invoice.databinding.FragmentJugSectionBinding
import com.google.android.material.button.MaterialButton

class JugSectionFragment : Fragment() {

    private var _binding: FragmentJugSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentJugSectionBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        onClickListener()
        initData()

        return binding.root
    }

    private fun initData() {
        val prefs: SharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val totalJug = prefs.getString("totaljugQty","100")
        binding.tvTotalJug.text = totalJug
    }

    private fun onClickListener() {
        binding.tvTotalJug.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.tvTotalJug -> {
                    showAlertDialog()
                }
            }
        }
    }


    private fun showAlertDialog() {

        val dialog = Dialog(requireContext(), R.style.RoundedCornersDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert_dialog_add_total_bottles)

        val btnDone : MaterialButton = dialog.findViewById(R.id.btnDoneWater)
        val tvTitleAdd : TextView = dialog.findViewById(R.id.tvTitleAdd)
        val etTotalJug : EditText = dialog.findViewById(R.id.etTotalBottleWater)

        tvTitleAdd.text = "Total Jug"
        etTotalJug.hint = "Enter total jug"

        dialog.show()

        val prefs: SharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val totalBottle = prefs.getString("totaljugQty","100")

        etTotalJug.setText(totalBottle)

        dialog.show()

        btnDone.setOnClickListener{
            if (etTotalJug.text.isEmpty()){
                etTotalJug.error = getString(R.string.error)
                return@setOnClickListener
            }
            else
            {
                val prefs1: SharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                prefs1.edit().putString("totaljugQty", etTotalJug.text.toString()).apply()
                initData()
                dialog.dismiss()
            }

        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
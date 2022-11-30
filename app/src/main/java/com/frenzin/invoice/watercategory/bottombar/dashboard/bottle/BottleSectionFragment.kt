package com.frenzin.invoice.watercategory.bottombar.dashboard.bottle

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.frenzin.invoice.R
import com.frenzin.invoice.databinding.FragmentBottleSectionBinding
import com.google.android.material.button.MaterialButton

class BottleSectionFragment : Fragment() {

    private var _binding: FragmentBottleSectionBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBottleSectionBinding.inflate(inflater, container, false)
        container!!.removeAllViews()


        onClickListener()
        initData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun onClickListener() {
        binding.tvTotalBottle.setOnClickListener(onClickListener)
    }

    private fun initData() {
        val prefs: SharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val totalBottle = prefs.getString("totalBottleQty","100")
        binding.tvTotalBottle.text = totalBottle.toString()
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.tvTotalBottle -> {
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
        val etTotalBottle : EditText = dialog.findViewById(R.id.etTotalBottleWater)

        val prefs: SharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val totalBottle = prefs.getString("totalBottleQty","100")

        etTotalBottle.setText(totalBottle)

        dialog.show()

        btnDone.setOnClickListener{
            if (etTotalBottle.text.isEmpty()){
                etTotalBottle.error = getString(R.string.error)
                return@setOnClickListener
            }
            else
            {
                val prefs1: SharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                prefs1.edit().putString("totalBottleQty", etTotalBottle.text.toString()).apply()
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

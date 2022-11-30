package com.frenzin.invoice.teacategory.bottombar.dashboard.coffee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frenzin.invoice.databinding.FragmentCoffeeSectionBinding

class CoffeeSectionFragment : Fragment() {

    private var _binding: FragmentCoffeeSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCoffeeSectionBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
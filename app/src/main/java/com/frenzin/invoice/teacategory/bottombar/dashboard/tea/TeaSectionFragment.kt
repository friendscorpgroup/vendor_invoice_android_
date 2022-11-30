package com.frenzin.invoice.teacategory.bottombar.dashboard.tea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frenzin.invoice.databinding.FragmentTeaSectionBinding

class TeaSectionFragment : Fragment(){

    private var _binding: FragmentTeaSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTeaSectionBinding.inflate(inflater, container, false)
        container!!.removeAllViews()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.vietquoc.ecommerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.databinding.FragmentSearchBinding
import com.vietquoc.ecommerce.util.showBottomNavigationView

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}
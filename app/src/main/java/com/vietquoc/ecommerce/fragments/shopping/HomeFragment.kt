package com.vietquoc.ecommerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.adapters.HomeViewpagerAdapter
import com.vietquoc.ecommerce.databinding.FragmentHomeBinding
import com.vietquoc.ecommerce.fragments.categories.AccessoryFragment
import com.vietquoc.ecommerce.fragments.categories.ChairFragment
import com.vietquoc.ecommerce.fragments.categories.CupboardFragment
import com.vietquoc.ecommerce.fragments.categories.FurnitureFragment
import com.vietquoc.ecommerce.fragments.categories.MainCategoryFragment
import com.vietquoc.ecommerce.fragments.categories.TableFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewpagerAdapter =
            HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewpagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Accessory"
                4 -> tab.text = "Table"
                5 -> tab.text = "Furniture"
            }
        }.attach()
    }
}
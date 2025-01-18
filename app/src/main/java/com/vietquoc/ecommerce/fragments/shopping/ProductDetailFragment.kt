package com.vietquoc.ecommerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.adapters.ColorAdapter
import com.vietquoc.ecommerce.adapters.SizeAdapter
import com.vietquoc.ecommerce.adapters.ViewPager2ImageAdapter
import com.vietquoc.ecommerce.databinding.FragmentProductDetailBinding
import com.vietquoc.ecommerce.util.hideBottomNavigationView

class ProductDetailFragment : Fragment() {

    private val args by navArgs<ProductDetailFragmentArgs>()
    private lateinit var binding: FragmentProductDetailBinding
    private val viewPagerAdapter by lazy { ViewPager2ImageAdapter() }
    private val sizeAdapter by lazy { SizeAdapter() }
    private val colorAdapter by lazy { ColorAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
        binding = FragmentProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizeRv()
        setupColorRv()
        setupViewpager()

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description
            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorAdapter.differ.submitList(product.colors)
        }
        product.sizes?.let {
            sizeAdapter.differ.submitList(product.sizes)
        }

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImage.adapter = viewPagerAdapter
        }
    }

    private fun setupColorRv() {
        binding.rvColors.apply {
            adapter = colorAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizeRv() {
        binding.rvSizes.apply {
            adapter = sizeAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}
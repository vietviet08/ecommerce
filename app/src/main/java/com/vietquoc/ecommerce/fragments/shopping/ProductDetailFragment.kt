package com.vietquoc.ecommerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.adapters.ColorAdapter
import com.vietquoc.ecommerce.adapters.SizeAdapter
import com.vietquoc.ecommerce.adapters.ViewPager2ImageAdapter
import com.vietquoc.ecommerce.data.CartProduct
import com.vietquoc.ecommerce.databinding.FragmentProductDetailBinding
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.util.hideBottomNavigationView
import com.vietquoc.ecommerce.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val args by navArgs<ProductDetailFragmentArgs>()
    private lateinit var binding: FragmentProductDetailBinding
    private val viewPagerAdapter by lazy { ViewPager2ImageAdapter() }
    private val sizeAdapter by lazy { SizeAdapter() }
    private val colorAdapter by lazy { ColorAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailViewModel>()

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

        sizeAdapter.onItemClick = {
            selectedSize = it
        }

        colorAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCart.setOnClickListener {
            if (sizeAdapter.differ.currentList.isNotEmpty() && selectedSize == null ||
                colorAdapter.differ.currentList.isNotEmpty() && selectedColor == null
            ) {
                Toast.makeText(requireContext(), "Please select size and color", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonAddToCart.revertAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {
                        binding.buttonAddToCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
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
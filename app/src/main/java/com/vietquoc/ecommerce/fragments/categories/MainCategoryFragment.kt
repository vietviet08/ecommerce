package com.vietquoc.ecommerce.fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.adapters.BestDealsAdapter
import com.vietquoc.ecommerce.adapters.BestProductAdapter
import com.vietquoc.ecommerce.adapters.SpecialProductsAdapter
import com.vietquoc.ecommerce.databinding.FragmentMainCategoryBinding
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        setupBestDealsProductRv()
        setupBestProductRv()

        lifecycleScope.launchWhenCreated {
            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.bestDealProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBarToLoadProduct.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestProductAdapter.differ.submitList(it.data)
                        binding.progressBarToLoadProduct.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressBarToLoadProduct.visibility = View.GONE
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchBestProduct()
            }
        }
        )
    }

    private fun setupBestProductRv() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProduct.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsProductRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }

    }

    private fun hideLoading() {
        binding.progressBarMainCategory.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBarMainCategory.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

}
package com.vietquoc.ecommerce.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.adapters.BestProductAdapter
import com.vietquoc.ecommerce.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment() {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        setupBestProductsRv()

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                onBestProductPagingRequest()
            }
        })
    }

    fun showOfferLoading(){
        binding.offerProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.offerProgressBar.visibility = View.GONE

    }

    fun showBestProductLoading(){
        binding.bestProductProgressBar.visibility = View.VISIBLE

    }

    fun hideBestProductLoading(){
        binding.bestProductProgressBar.visibility = View.GONE

    }
    open fun onOfferPagingRequest() {}
    open fun onBestProductPagingRequest() {}

    private fun setupOfferRv() {
        binding.rvOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }

    private fun setupBestProductsRv() {
        binding.rvBestProduct.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }
}
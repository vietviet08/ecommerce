package com.vietquoc.ecommerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.adapters.AddressAdapter
import com.vietquoc.ecommerce.adapters.BillingProductAdapter
import com.vietquoc.ecommerce.data.Address
import com.vietquoc.ecommerce.data.CartProduct
import com.vietquoc.ecommerce.data.order.Order
import com.vietquoc.ecommerce.data.order.OrderStatus
import com.vietquoc.ecommerce.databinding.FragmentBillingBinding
import com.vietquoc.ecommerce.util.HorizontalItemDecoration
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.viewmodel.BillingViewModel
import com.vietquoc.ecommerce.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products = args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddressRv()
        setupBillingProductsRv()

        if(!args.payment){
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
            }
        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }

        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = "$ $totalPrice"

        addressAdapter.onClick = {
            selectedAddress = it
            if (!args.payment) {
                val b = Bundle().apply {
                    putParcelable("address", selectedAddress)
                }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, b)
            }
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }

        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(
                            requireView(),
                            "Your order was placed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = android.app.AlertDialog.Builder(requireContext()).apply {
            setTitle("Order your cart")
            setMessage("Do you want order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply {
            adapter = billingProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration())
        }
    }
}
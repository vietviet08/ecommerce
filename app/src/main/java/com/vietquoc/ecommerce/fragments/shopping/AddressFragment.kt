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
import com.google.android.material.snackbar.Snackbar
import com.vietquoc.ecommerce.data.Address
import com.vietquoc.ecommerce.databinding.FragmentAddressBinding
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    private val args by navArgs<AddressFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.editAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Snackbar.make(
                            requireView(),
                            "Address successfully edited",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Snackbar.make(
                            requireView(),
                            "Address deleted successfully",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addressFocus = args.address

        binding.apply {
            buttonDelelte.visibility = if (addressFocus == null) View.GONE else View.VISIBLE

            addressFocus?.let {
                edAddressTitle.setText(it.addressTitle)
                edFullName.setText(it.fullName)
                edStreet.setText(it.street)
                edPhone.setText(it.phone)
                edCity.setText(it.city)
                edState.setText(it.state)
            }

            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString().trim()
                val fullName = edFullName.text.toString().trim()
                val street = edStreet.text.toString().trim()
                val phone = edPhone.text.toString().trim()
                val city = edCity.text.toString().trim()
                val state = edState.text.toString().trim()

                if (addressTitle.isBlank() || fullName.isBlank() || street.isBlank() || phone.isBlank() || city.isBlank() || state.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val address = Address(addressTitle, fullName, street, phone, city, state)

                if (addressFocus == null) {
                    viewModel.addAddress(address)
                } else {
                    viewModel.editAddress(address.copy(addressId = addressFocus.addressId))
                }
            }

            imageAddressClose.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonDelelte.setOnClickListener {
                addressFocus?.let {
                    val alertDialog = android.app.AlertDialog.Builder(requireContext()).apply {
                        setTitle("Delete address")
                        setMessage("Do you want to delete this address?")
                        setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("Yes") { dialog, _ ->
                            viewModel.deleteAddress(it)
                            dialog.dismiss()
                        }
                    }
                    alertDialog.create()
                    alertDialog.show()
                } ?: run {
                    Toast.makeText(requireContext(), "No address to delete", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}
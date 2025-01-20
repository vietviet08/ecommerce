package com.vietquoc.ecommerce.fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vietquoc.ecommerce.BuildConfig
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.activites.LoginRegisterActivity
import com.vietquoc.ecommerce.databinding.FragmentProfileBinding
import com.vietquoc.ecommerce.databinding.FragmentSearchBinding
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.util.showBottomNavigationView
import com.vietquoc.ecommerce.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrderFragment)
        }

        binding.linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                0f,
                emptyArray(),
                false
            )
            findNavController().navigate(action)
        }

        binding.linearLogOut.setOnClickListener {
            showConfirmationLogoutDialog()
        }

        binding.tvVersion.text = "Version ${BuildConfig.VERSION_CODE}"

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(
                            ColorDrawable(
                                Color.BLACK
                            )
                        ).into(binding.imageUser)
                        binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarSettings.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    private fun showConfirmationLogoutDialog() {
        val alertDialog = android.app.AlertDialog.Builder(requireContext()).apply {
            setTitle("Logout")
            setMessage("Do you want logout your account?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()
                val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        alertDialog.create()
        alertDialog.show()

    }
}
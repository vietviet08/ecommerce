package com.vietquoc.ecommerce.fragments.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.activites.ShoppingActivity
import com.vietquoc.ecommerce.databinding.FragmentLoginBinding
import com.vietquoc.ecommerce.dialog.setupBottomSetDialog
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewmodel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButtonPageLogin.setOnClickListener {
                val email = editTextEmailLogin.text.toString().trim()
                val password = editTextPasswordLogin.text.toString().trim()
                viewmodel.login(email, password)
            }

            textViewGoToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            forgotPassword.setOnClickListener {
                setupBottomSetDialog { email ->
                    viewmodel.resetPassword(email)
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewmodel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.loginButtonPageLogin.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.loginButtonPageLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        binding.loginButtonPageLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewmodel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.loginButtonPageLogin.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.loginButtonPageLogin.revertAnimation()
                        Snackbar.make(
                            requireView(),
                            "Reset link was sent to your email",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        binding.loginButtonPageLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }
            }
        }

    }

}
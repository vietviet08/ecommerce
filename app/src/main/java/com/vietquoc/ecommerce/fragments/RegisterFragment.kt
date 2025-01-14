package com.vietquoc.ecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vietquoc.ecommerce.R
import com.vietquoc.ecommerce.data.User
import com.vietquoc.ecommerce.databinding.FragmentRegisterBinding
import com.vietquoc.ecommerce.util.RegisterValidation
import com.vietquoc.ecommerce.util.Resource
import com.vietquoc.ecommerce.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerButtonPageRegister.setOnClickListener {
                val user = User(
                    editTextFistNameRegister.text.toString().trim(),
                    editTextLastNameRegister.text.toString().trim(),
                    editTextEmailRegister.text.toString().trim(),
                )
                val password = editPasswordRegister.text.toString().trim()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }

            textViewBackToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.registerButtonPageRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d("register success", it.data.toString())
                        Toast.makeText(
                            requireContext(),
                            "Welcome to the ecommerce shop, happy shopping!",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.registerButtonPageRegister.revertAnimation()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.registerButtonPageRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    binding.editTextEmailRegister.apply {
                        requestFocus()
                        error = validation.email.message
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    binding.editPasswordRegister.apply {
                        requestFocus()
                        error = validation.password.message
                    }
                }
            }
        }

    }

}
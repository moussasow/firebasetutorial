package com.mas.firebasetutorial.ui.fragment.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mas.firebasetutorial.R
import com.mas.firebasetutorial.databinding.FragmentMainBinding
import com.mas.firebasetutorial.ui.fragment.BaseFragment
import com.mas.firebasetutorial.ui.fragment.login.LoginFragment

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogin.setOnClickListener {
                transitFragment(LoginFragment.newInstance(LoginFragment.AUTH_TYPE_LOGIN))
            }

            btnRegister.setOnClickListener {
                transitFragment(LoginFragment.newInstance(LoginFragment.AUTH_TYPE_REGISTER))
            }
        }

    }
}
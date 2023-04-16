package com.mas.firebasetutorial.ui.fragment.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.mas.firebasetutorial.R
import com.mas.firebasetutorial.databinding.FragmentTopBinding
import com.mas.firebasetutorial.ui.fragment.BaseFragment
import com.mas.firebasetutorial.ui.fragment.login.LoginFragment

private const val ARG_USERNAME = "ARG_USERNAME"
private const val ARG_DISPLAY_NAME = "ARG_DISPLAY_NAME"

/**
 * Use the [TopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(username: String, name: String) =
            TopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                    putString(ARG_DISPLAY_NAME, name)
                }
            }
    }
    private lateinit var binding: FragmentTopBinding
    private var userName: String? = null
    private var displayName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(ARG_USERNAME)
            displayName = it.getString(ARG_DISPLAY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            transitFragment(LoginFragment.newInstance(LoginFragment.AUTH_TYPE_LOGIN))
        }
    }

    private fun updateUi() {
        binding.apply {
            textName.text = displayName
        }
    }

}
package com.mas.firebasetutorial.ui.fragment.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mas.firebasetutorial.common.Utility
import com.mas.firebasetutorial.common.Utility.createUserDbRef
import com.mas.firebasetutorial.databinding.FragmentTopBinding
import com.mas.firebasetutorial.ui.fragment.BaseFragment
import com.mas.firebasetutorial.ui.fragment.login.LoginFragment
import com.mas.firebasetutorial.ui.fragment.top.data.UserModel

private const val ARG_USERNAME = "ARG_USERNAME"
private const val ARG_DISPLAY_NAME = "ARG_DISPLAY_NAME"

/**
 * Use the [TopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(username: String?, name: String?) =
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
        requestFromDatabase()
        showTitle("Top")
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            transitFragment(LoginFragment.newInstance(LoginFragment.AUTH_TYPE_LOGIN))
        }

        binding.apply {
            btnRecord.setOnClickListener {
                val username = editUsername.text.toString()
                if (Utility.checkUserName(username, it)) {
                    binding.progressBar.visibility = View.VISIBLE
                    val map: HashMap<String, Any> = HashMap()
                    map["Username"] = username
                    map["Phone"] = editPhone.text.toString()
                    createDatabase(map)
                }
            }

            btnUserUpdate.setOnClickListener {

            }
        }
    }

    private fun createDatabase(map: HashMap<String, Any>) {
        createUserDbRef(userName).updateChildren(map).addOnCompleteListener {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun requestFromDatabase() {
        binding.progressBar.visibility = View.VISIBLE
        val databaseReference = createUserDbRef(userName)

        databaseReference.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(UserModel::class.java)
                if (data == null) {
                    updateUi("")
                } else {
                    updateUi(data.Username)
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                updateUi("")
                binding.progressBar.visibility = View.GONE
            }
        })


    }
    private fun updateUi(username: String) {
        binding.apply {
            textName.text = username
        }

        setViewsVisibility (username.isEmpty())
    }

    private fun setViewsVisibility(isNew : Boolean) {
        binding.apply {
            layoutUser.isVisible = isNew
            textWelcome.isVisible = isNew
            editUsername.isVisible = isNew
            editPhone.isVisible = isNew
            btnRecord.isVisible = isNew
            layoutUser.isVisible = !isNew
        }
    }
}
package com.mas.firebasetutorial.ui.fragment

import androidx.fragment.app.Fragment
import com.mas.firebasetutorial.R

open class BaseFragment : Fragment() {

    fun transitFragment(fragment: BaseFragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commitNow()
    }
}
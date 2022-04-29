package com.example.android.finalyearproject.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.FragmentRegisterSelectRoleBinding

class SelectRoleFragment: Fragment() {

    private lateinit var binding: FragmentRegisterSelectRoleBinding

    private lateinit var btnSelectCustomer: Button
    private lateinit var btnSelectMech: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterSelectRoleBinding.inflate(layoutInflater)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentRegisterSelectRoleBinding) {
        btnSelectCustomer = binding.btnSelectRoleCustomer
        btnSelectMech = binding.btnSelectRoleMech
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnSelectCustomer.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_navigation_register_select_role_to_navigation_register)
        }
        btnSelectMech.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_navigation_register_select_role_to_navigation_register_mech)
        }
    }
}
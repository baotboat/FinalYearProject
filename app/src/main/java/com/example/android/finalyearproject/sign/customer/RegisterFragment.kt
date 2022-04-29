package com.example.android.finalyearproject.sign.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.OnCreateUserListener
import com.example.android.finalyearproject.databinding.FragmentRegisterBinding
import com.example.android.finalyearproject.databinding.FragmentRegisterMechBinding
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository
import com.example.android.finalyearproject.sign.mech.RegisterMechViewmodel
import com.example.android.finalyearproject.sign.mech.RegisterMechViewmodelFactory
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment: Fragment(), OnCreateUserListener {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var edtName: EditText
    private lateinit var edtSurname: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var edtDesc: EditText
    private lateinit var btnSumbit: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        findView()

        return binding.root
    }

    private fun findView() {
        edtName = binding.edtRegisterName
        edtSurname = binding.edtRegisterSurname
        edtPhone = binding.edtRegisterPhone
        edtAddress = binding.edtRegisterAddress
        edtEmail = binding.edtRegisterEmail
        edtPassword = binding.edtRegisterPassword
        edtConfirmPassword = binding.edtRegisterConfirmPassword
        edtDesc = binding.edtRegisterDesc
        btnSumbit = binding.btnRegisterSumbit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnSumbit.setOnClickListener {
            val user = User(
                name = edtName.text.toString(),
                surname = edtSurname.text.toString(),
                phone = edtPhone.text.toString(),
                address = edtAddress.text.toString(),
                email = edtEmail.text.toString(),
                password = edtPassword.text.toString(),
                confirmPassword = edtConfirmPassword.text.toString(),
                desc = edtDesc.text.toString(),
                role = "ลูกค้า"
            )
            onCreateUser(user)
        }
    }

    override fun onCreateUser(user: User) {
        (activity as? OnCreateUserListener)?.onCreateUser(user)
    }
}
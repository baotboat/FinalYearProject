package com.example.android.finalyearproject.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.android.finalyearproject.MainActivity
import com.example.android.finalyearproject.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private lateinit var  auth: FirebaseAuth

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var tvRegister: TextView
    private lateinit var tvForgetPassword: TextView
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth= FirebaseAuth.getInstance()
        findView()
        initView()
    }

    private fun findView() {
        edtUsername = binding.edtUsername
        edtPassword = binding.edtPassword
        tvRegister = binding.tvSignInRegister
        btnLogin = binding.btnLogin
        tvForgetPassword = binding.tvSignInForgetPassword
    }

    private fun initView() {
        tvRegister.setOnClickListener {
            register()
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
        tvForgetPassword.setOnClickListener {
            DialogForgetPassword().show(supportFragmentManager, "DialogForgetPassword")
        }
    }

    private fun register() {
        val intent= Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login() {
        val email = edtUsername.text.toString()
        val password = edtPassword.text.toString()
        if(email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "กรุณากรอกอีเมลและรหัสผ่าน", Toast.LENGTH_LONG)
                .show()
        } else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { e ->
                when(e.localizedMessage) {
                    "There is no user record corresponding to this identifier. The user may have been deleted." ->
                        Toast.makeText(applicationContext, "อีเมลหรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show()
                    "The password is invalid or the user does not have a password." ->
                        Toast.makeText(applicationContext, "อีเมลหรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show()
                    "The email address is badly formatted." ->
                        Toast.makeText(applicationContext, "อีเมลหรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}
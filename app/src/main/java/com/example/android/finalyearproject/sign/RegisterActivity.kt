package com.example.android.finalyearproject.sign

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.finalyearproject.MainActivity
import com.example.android.finalyearproject.OnCreateUserListener
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity(), OnCreateUserListener {

    val EXTRA_USER = "com.example.android.finalyearproject.sign.User"
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
    }

    fun createAuthUser(user: User) {
        var correctFormat = false
        val suffixGmail = "@gmail.com"
        val suffixHotmail = "@hotmail.com"
        val suffixYahoo = "@yahoo.com"
        val email = user.email ?: "null"
        val password = user.password ?: "null"
        if (email.endsWith(suffixGmail)) {
            correctFormat = true
        } else if (email.endsWith(suffixHotmail)) {
            correctFormat = true
        } else if (email.endsWith(suffixYahoo)) {
            correctFormat = true
        }
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "กรุณากรอกอีเมลและรหัสผ่าน", Toast.LENGTH_LONG)
                .show()
        } else if ((!correctFormat)) {
            Toast.makeText(applicationContext, "กรุณากรอกรูปแบบอีเมลให้ถูกต้อง", Toast.LENGTH_LONG)
                .show()
        } else if (password.length < 6) {
            Toast.makeText(applicationContext,
                "กรุณากรอกรหัสผ่านอย่างน้อง 6 ตัวอักษร",
                Toast.LENGTH_LONG).show()
        } else if (user.password != user.confirmPassword) {
            Toast.makeText(applicationContext, "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_LONG).show()
        } else if (user.role == "ช่าง") {
            if (user.name.isNullOrEmpty() || user.surname.isNullOrEmpty() ||
                user.phone.isNullOrEmpty() || user.address.isNullOrEmpty() ||
                user.desc.isNullOrEmpty() || user.type.isNullOrEmpty()
            ) {
                Toast.makeText(applicationContext,
                    "กรุณากรอกข้อมูลให้ครบทุกช่อง",
                    Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra(EXTRA_USER, user)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        when (e.localizedMessage) {
                            "The email address is already in use by another account." -> Toast.makeText(
                                applicationContext,
                                "อีเมลนี้ถูกใช้งานแล้ว",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        } else if (user.role == "ลูกค้า") {
            if (user.name.isNullOrEmpty() || user.surname.isNullOrEmpty() ||
                user.phone.isNullOrEmpty() || user.address.isNullOrEmpty() ||
                user.desc.isNullOrEmpty()
            ) {
                Toast.makeText(applicationContext,
                    "กรุณากรอกข้อมูลให้ครบทุกช่อง",
                    Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra(EXTRA_USER, user)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        when (e.localizedMessage) {
                            "The email address is already in use by another account." -> Toast.makeText(
                                applicationContext,
                                "อีเมลนี้ถูกใช้งานแล้ว",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    override fun onCreateUser(user: User) {
        createAuthUser(user)
    }
}
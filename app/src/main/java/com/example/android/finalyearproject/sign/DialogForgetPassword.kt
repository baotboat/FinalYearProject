package com.example.android.finalyearproject.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.android.finalyearproject.databinding.DialogForgetPasswordBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DialogForgetPassword : DialogFragment() {
    private lateinit var binding: DialogForgetPasswordBinding

    private lateinit var edtEmail: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvPleaseFillEmail: TextView
    private lateinit var tvUnCorrectFormat: TextView
    private lateinit var tvNotFoundEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogForgetPasswordBinding.inflate(layoutInflater)
        findView()

        return binding.root
    }

    private fun findView() {
        edtEmail = binding.edtEmailForgetPassword
        btnSubmit = binding.btnDialogForgetPasswordSumbit
        tvPleaseFillEmail = binding.tvPleaseFillEmail
        tvUnCorrectFormat = binding.tvUnCorrectFormat
        tvNotFoundEmail = binding.tvNotFoundEmail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnSubmit.setOnClickListener {
            val email = edtEmail.text.toString()
            var correctFormat = false
            val suffixGmail = "@gmail.com"
            val suffixHotmail = "@hotmail.com"
            val suffixYahoo = "@yahoo.com"
            if (email.endsWith(suffixGmail)) {
                correctFormat = true
            } else if (email.endsWith(suffixHotmail)) {
                correctFormat = true
            } else if (email.endsWith(suffixYahoo)) {
                correctFormat = true
            }
            if (email.isNullOrEmpty()) {
                tvPleaseFillEmail.visibility = View.VISIBLE
                tvUnCorrectFormat.visibility = View.GONE
            } else if (!correctFormat) {
                tvUnCorrectFormat.visibility = View.VISIBLE
                tvPleaseFillEmail.visibility = View.GONE
            } else {
                forgetPassword(email)
            }
        }
    }

    private fun forgetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dialog?.dismiss()
                }
            }
            .addOnFailureListener { e ->
                when(e.localizedMessage) {
                    "There is no user record corresponding to this identifier. The user may have been deleted." ->
                        tvNotFoundEmail.visibility = View.VISIBLE
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val width = (ViewGroup.LayoutParams.MATCH_PARENT)
        val height = (resources.displayMetrics.heightPixels * 0.45).toInt()
        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setTitle("Forget Password")
    }
}
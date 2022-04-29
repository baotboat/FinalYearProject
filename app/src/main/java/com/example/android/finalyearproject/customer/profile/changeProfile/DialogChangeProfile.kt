package com.example.android.finalyearproject.customer.profile.changeProfile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.android.finalyearproject.customer.createTask.UploadImageContract
import com.example.android.finalyearproject.databinding.DialogChangeProfileBinding
import com.example.android.finalyearproject.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DialogChangeProfile(
    val userData: User,
    private val profileFragmentListener: ProfileFragmentListener,
    ) : DialogFragment() {
    private lateinit var binding: DialogChangeProfileBinding

    private lateinit var btnChangeProfileImg: Button
    private lateinit var btnChangeProfileSubmit: Button
    private lateinit var edtDesc: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtPhone: EditText
    private lateinit var tvNotFillAllFields: TextView
    private lateinit var imgPreview: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var imgProfileLoading: ImageView
    private var uriImage: Uri? = null
    private val selectImageFromGalleryResult =
        registerForActivityResult(UploadImageContract()) { uri: Uri? ->
            uri?.let {
                uriImage = uri
                imgPreview.setImageURI(uri)
                imgProfile.visibility = View.INVISIBLE
            }
        }
    private var firebaseStorage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogChangeProfileBinding.inflate(layoutInflater)
        findView()

        return binding.root
    }

    private fun findView() {
        btnChangeProfileImg = binding.btnChangeProfileChangeImg
        btnChangeProfileSubmit = binding.btnChangeProfileSubmit
        edtDesc = binding.edtChangeProfileDesc
        edtAddress = binding.edtChangeProfileOldAddress
        edtPhone = binding.edtChangeProfilePhone
        tvNotFillAllFields = binding.tvChangeProfileNotFillAllFields
        imgPreview = binding.imgChangeProfileimagePreview
        imgProfile = binding.imgChangeProfile
        imgProfileLoading = binding.imgChangeProfileLoading
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        displayImage()
        edtDesc.setText(userData.desc)
        edtAddress.setText(userData.address)
        edtPhone.setText(userData.phone)
        btnChangeProfileSubmit.setOnClickListener {
            if (edtDesc.text.toString().isNullOrEmpty() ||
                edtAddress.text.toString().isNullOrEmpty() ||
                edtPhone.text.toString().isNullOrEmpty()
            ) {
                tvNotFillAllFields.visibility = View.VISIBLE
            } else {
                uriImage?.let {
                    profileFragmentListener.changeProfileImage(uriImage!!)
                }
                profileFragmentListener.changeUserData(
                    edtDesc.text.toString(),
                    edtAddress.text.toString(),
                    edtPhone.text.toString(),
                )
                dialog?.dismiss()
            }
        }
        btnChangeProfileImg.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun displayImage() {
        imgProfile.visibility = View.INVISIBLE
        imgProfileLoading.visibility = View.VISIBLE
        val storageReference = firebaseStorage.reference.child("images/${userData.uId}")
        val IMAGE_SIZE: Long = 4096 * 4096
        storageReference.getBytes(IMAGE_SIZE)
            .addOnSuccessListener {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                imgProfile.setImageBitmap(Bitmap.createScaledBitmap(
                    bmp,
                    imgProfile.width,
                    imgProfile.height,
                    false))
                imgProfile.visibility = View.VISIBLE
                imgProfileLoading.visibility = View.INVISIBLE
            }
            .addOnFailureListener {
                imgProfile.visibility = View.VISIBLE
                imgProfileLoading.visibility = View.INVISIBLE
            }
    }

    override fun onStart() {
        super.onStart()
        val width = (ViewGroup.LayoutParams.MATCH_PARENT)
        val height = (resources.displayMetrics.heightPixels * 0.75).toInt()
        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setTitle("Change Profile")
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")
}
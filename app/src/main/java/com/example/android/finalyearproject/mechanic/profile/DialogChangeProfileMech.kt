package com.example.android.finalyearproject.mechanic.profile

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
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.customer.createTask.UploadImageContract
import com.example.android.finalyearproject.customer.profile.changeProfile.ProfileFragmentListener
import com.example.android.finalyearproject.databinding.DialogChangeProfileMechBinding
import com.example.android.finalyearproject.model.User
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DialogChangeProfileMech(
    val userData: User,
    private val profileFragmentListener: ProfileFragmentListener,
) : DialogFragment() {
    private lateinit var binding: DialogChangeProfileMechBinding

    private lateinit var btnChangeProfileMechImg: Button
    private lateinit var btnChangeProfileMechSubmit: Button
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
    var firebaseStorage = Firebase.storage
    private lateinit var groupChipType: ChipGroup
    private lateinit var allTypeChip: List<Chip>

    enum class ChipType(val data: List<String>) {
        TYPE(listOf("ตู้เย็น",
            "ทีวี",
            "พัดลม",
            "แอร์",
            "เครื่องซักผ้า"
        ))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogChangeProfileMechBinding.inflate(layoutInflater)
        findView()

        return binding.root
    }

    private fun findView() {
        btnChangeProfileMechImg = binding.btnChangeProfileMechChangeImg
        btnChangeProfileMechSubmit = binding.btnChangeProfileMechSubmit
        edtDesc = binding.edtChangeProfileMechDesc
        edtAddress = binding.edtChangeProfileMechAddress
        edtPhone = binding.edtChangeProfileMechPhone
        tvNotFillAllFields = binding.tvChangeProfileMechNotFillAllFields
        imgPreview = binding.imgChangeProfileMechimagePreview
        imgProfile = binding.imgChangeProfileMech
        imgProfileLoading = binding.imgChangeProfileMechLoading
        groupChipType = binding.groupChipType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setChipSymptom()
        userData.type?.let { selectedChipFromAppliedType(it) }
    }

    private fun initView() {
        displayImage()
        edtDesc.setText(userData.desc)
        edtAddress.setText(userData.address)
        edtPhone.setText(userData.phone)
        btnChangeProfileMechSubmit.setOnClickListener {
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
                    checkSelectedTypeChip()
                )
                dialog?.dismiss()
            }
        }
        btnChangeProfileMechImg.setOnClickListener {
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
                var bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
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
        dialog!!.window?.setTitle("Change Profile Mech")
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun setChipSymptom() {
        val inflator = LayoutInflater.from(groupChipType.context)
        val groupChip = ChipType.TYPE.data.map { type ->
            val chip = inflator.inflate(R.layout.item_chip_type, groupChipType, false) as Chip
            chip.text = type
            chip.tag = type
            chip
        }
        allTypeChip = groupChip
        groupChipType.removeAllViews()
        groupChip.onEach { groupChipType.addView(it) }
    }

    private fun selectedChipFromAppliedType(type: List<String>) {
        val appliedTypeChip = allTypeChip.filter { type.contains(it.text.toString()) }
        appliedTypeChip.onEach { it.isChecked = true }
    }

    private fun checkSelectedTypeChip(): List<String> {
        val result = mutableListOf<String>()
        allTypeChip.filter { it.isChecked }.onEach { result.add(it.text.toString()) }
        return result
    }
}
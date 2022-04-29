package com.example.android.finalyearproject.sign.mech

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.*
import com.example.android.finalyearproject.databinding.FragmentRegisterMechBinding
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth

class RegisterMechFragment: Fragment(), OnCreateUserListener {

    private lateinit var binding: FragmentRegisterMechBinding
    private lateinit var registerMechViewmodel: RegisterMechViewmodel
    private lateinit var registerMechViewmodelFactory: RegisterMechViewmodelFactory

    private lateinit var edtName: EditText
    private lateinit var edtSurname: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var edtDesc: EditText
    private lateinit var chipGroupType: ChipGroup
    private lateinit var btnSumbit: Button
    private lateinit var allChipType: List<Chip>

    enum class ChipType(val data: List<String>) {
        TYPE (listOf("ตู้เย็น",
            "ทีวี",
            "พัดลม",
            "แอร์",
            "เครื่องซักผ้า"
        ))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterMechBinding.inflate(layoutInflater)
        var repo = UserRepository()
        registerMechViewmodelFactory = RegisterMechViewmodelFactory(repo)
        registerMechViewmodel = ViewModelProvider(this, registerMechViewmodelFactory)
            .get(RegisterMechViewmodel::class.java)
        findView()

        return binding.root
    }

    private fun findView() {
        edtName = binding.edtRegisterMechName
        edtSurname = binding.edtRegisterMechSurname
        edtPhone = binding.edtRegisterMechPhone
        edtAddress = binding.edtRegisterMechAddress
        edtEmail = binding.edtRegisterMechEmail
        edtPassword = binding.edtRegisterMechPassword
        edtConfirmPassword = binding.edtRegisterMechConfirmPassword
        edtDesc = binding.edtRegisterMechDesc
        chipGroupType = binding.chipGroupType
        btnSumbit = binding.btnRegisterMechSumbit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setChoiceChipSymptom()
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
                role = "ช่าง",
                type = checkSelectedChipType()
            )
            onCreateUser(user)
        }
    }

    override fun onCreateUser(user: User) {
        (activity as? OnCreateUserListener)?.onCreateUser(user)
    }

    private fun setChoiceChipSymptom() {
        val inflator = LayoutInflater.from(chipGroupType.context)
        val groupChip = ChipType.TYPE.data.map { type ->
            val chip = inflator.inflate(R.layout.item_chip_type, chipGroupType, false) as Chip
            chip.text = type
            chip.tag = type
            chip
        }
        allChipType = groupChip
        chipGroupType.removeAllViews()
        for (chip in groupChip) {
            chipGroupType.addView(chip)
        }
    }

    private fun checkSelectedChipType(): MutableList<String> {
        val result = mutableListOf<String>()
        allChipType.filter { it.isChecked}.onEach { result.add(it.text.toString()) }
        return result
    }

}
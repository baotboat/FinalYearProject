package com.example.android.finalyearproject.customer.createTask

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.FragmentCreateTaskBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.savvi.rangedatepicker.CalendarPickerView
import java.time.Instant
import java.util.*

class CreateTaskFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var createTaskViewModel: CreateTaskViewmodel
    private lateinit var createTaskViewModelFactory: CreateTaskViewmodelFactory

    private lateinit var typeSpinCreateTask: Spinner
    private lateinit var brandSpinCreateTask: Spinner
    private lateinit var chipGroupSymptom: ChipGroup
    private lateinit var submitBtn: Button
    private lateinit var cvUploadImage: CardView
    private lateinit var nowDate: Calendar
    private lateinit var endDate: Calendar
    private lateinit var calendar: CalendarPickerView
    private lateinit var type: String
    private lateinit var brand: String
    private lateinit var spec: EditText
    private lateinit var desc: EditText
    private lateinit var symptom: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var allChipSymptom: List<Chip>
    private lateinit var chipSymptom: String
    private lateinit var currentUser: FirebaseUser
    private lateinit var userData: User
    private lateinit var selectImageFromGalleryResult : ActivityResultLauncher<String>

    private var uriImage: Uri? = null


    enum class ChipSymptom(val data: List<String>) {
        REFRI(kotlin.collections.listOf("เปิดไม่ติด", "ไม่เย็น", "น้ำแข็งเกาะช่องฟรีซ", "น้ำแข็งเกาะผนังตู้เย็น", "ไฟไม่ติด", "ไม่มีเสียงทำงาน", "ประตูปิดไม่สนิท", "อื่นๆ")),
        FAN(kotlin.collections.listOf("เปิดไม่ติด", "ใบพัดไม่หมุน", "คอพัดลมไม่หมุน", "มีเสียงดัง", "หมุนช้า", "ใบพัดแตก", "ปุ่มเลือกเบอร์กดไม่ได้", "อื่นๆ")),
        AIR(kotlin.collections.listOf("เปิดไม่ติด", "แอร์ไม่เย็น", "เสียงดัง", "น้ำหยด", "มีกลิ่นเหม็น", "ใบพัดไม่สวิง", "อื่นๆ")),
        TV(kotlin.collections.listOf("เปิดไม่ติด", "ภาพกระตุก", "สีเพี้ยน", "ไม่มีเสียง", "ภาพไม่ขึ้น", "สัญญาณรบกวน", "อื่นๆ")),
        WASHMACH(kotlin.collections.listOf("เปิดไม่ติด", "ถังไม่หมุน", "ถังไม่หยุดหมุนตอนเปิดฝา", "น้ำรั่ว", "ไม่ระบายน้ำ", "ผงซักฟอกปนกับน้ำยาปรับผ้านุ่ม" , "ไม่ผสมผงซักฟอก", "ถังปั่นไม่หยุด", "อื่นๆ")),
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(layoutInflater)
        val taskRepo = TaskRepository()
        val userRepo = UserRepository()
        createTaskViewModelFactory = CreateTaskViewmodelFactory(taskRepo, userRepo )
        createTaskViewModel = ViewModelProvider(this, createTaskViewModelFactory)
            .get(CreateTaskViewmodel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentCreateTaskBinding) {
        typeSpinCreateTask = binding.typeSpinner
        brandSpinCreateTask = binding.brandSpinner
        chipGroupSymptom = binding.chipGroupSymptom
        calendar = binding.calendarView
        submitBtn = binding.btnSumbit
        spec = binding.edtSpec
        desc = binding.edtDesc
        symptom = binding.edtSymptom
        cvUploadImage = binding.cvImagePreview
        imgPreview = binding.imagePreview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = findCurrentUser()!!
        initView()
        initObserve()
        createTaskViewModel.getUserByUId(currentUser.uid)
    }

    private fun initView() {
        selectImageFromGalleryResult = registerForActivityResult(UploadImageContract()) { uri: Uri? ->
            uri?.let {
                imgPreview.setImageURI(uri)
                uriImage = uri
            }
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinCreateTask.adapter = adapter
        }
        typeSpinCreateTask.onItemSelectedListener = this
        brandSpinCreateTask.onItemSelectedListener = this
        submitBtn.setOnClickListener { view ->
            checkSelectedChipSymptom()
            if (spec.text.toString().isNullOrEmpty() || desc.text.toString().isNullOrEmpty() || symptom.text.toString().isNullOrEmpty() || chipSymptom == "null" ) {
                Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG).show()
            } else {
                submitBtn.isEnabled = false
                binding.imgLoading.visibility = View.VISIBLE
                val taskId = createTaskId()
                uriImage?.let { uploadImage(it, taskId, view) }
                createTask(taskId)
                if (uriImage == null) {
                    view.findNavController()
                        .navigate(R.id.action_createTaskFragment_to_navigation_find_mechanic)
                }
            }
        }

        nowDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
        endDate.set(2022, 11, 31)
        val selectedDate = Date()
        calendar.init(nowDate.time, endDate.time)
            .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
            .withSelectedDate(selectedDate)
        cvUploadImage.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun uploadImage(uri: Uri, taskId: String, view: View) {
        createTaskViewModel.uploadImage(uri, taskId, view)
    }

    private fun initObserve() {
        createTaskViewModel.getUserData.observe(viewLifecycleOwner) {
            userData = it
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun findCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private fun createTask(taskId: String) {
        createTaskViewModel.addTask(
            Task(
                taskId = taskId,
                creatorUId = userData.uId,
                creatorName = "${userData.name} ${userData.surname}",
                type = this.type,
                status = "กำลังหาช่าง",
                brand = this.brand,
                spec = this.spec.text.toString(),
                desc = this.desc.text.toString(),
                chipSymptom = chipSymptom,
                symptom = this.symptom.text.toString(),
                date = calendar.selectedDates,
                address = userData.address
            )
        )
    }

    private fun createTaskId(): String {
        return Instant.now().toString() + "." + suffixTaskId()
    }

    private fun suffixTaskId(): String {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..6)
            .asSequence()
            .map { chars.random() }
            .joinToString("")
    }

    private fun checkSelectedChipSymptom() {
        chipSymptom = allChipSymptom.find { it.isChecked}?.text.toString()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            typeSpinCreateTask.id -> {
                type = parent.getItemAtPosition(position).toString()
                checkSelectedType(type)
            }
            brandSpinCreateTask.id -> brand = parent.getItemAtPosition(position).toString()
        }
    }

    private fun checkSelectedType(selected: String) {
        when (selected) {
            "ตู้เย็น" -> {
                val array = R.array.brand_refri_array
                setChoiceBrandSpinner(array)
                setChoiceChipSymptom(ChipSymptom.REFRI.data)
            }
            "ทีวี" -> {
                val array = R.array.brand_tv_array
                setChoiceBrandSpinner(array)
                setChoiceChipSymptom(ChipSymptom.TV.data)
            }
            "พัดลม" -> {
                val array = R.array.brand_fan_array
                setChoiceBrandSpinner(array)
                setChoiceChipSymptom(ChipSymptom.FAN.data)
            }
            "แอร์" -> {
                val array = R.array.brand_air_array
                setChoiceBrandSpinner(array)
                setChoiceChipSymptom(ChipSymptom.AIR.data)
            }
            "เครื่องซักผ้า" -> {
                val array = R.array.brand_washing_machine_array
                setChoiceBrandSpinner(array)
                setChoiceChipSymptom(ChipSymptom.WASHMACH.data)
            }
        }
    }

    private fun setChoiceBrandSpinner(textArrayId: Int) {
        ArrayAdapter.createFromResource(
            requireContext(),
            textArrayId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            brandSpinCreateTask.adapter = adapter
        }
    }

    private fun setChoiceChipSymptom(data: List<String>) {
        val inflator = LayoutInflater.from(chipGroupSymptom.context)
        val groupChip = data.map { symptom ->
            val chip = inflator.inflate(R.layout.item_chip_symptom, chipGroupSymptom, false) as Chip
            chip.text = symptom
            chip.tag = symptom
            chip
        }
        allChipSymptom = groupChip
        chipGroupSymptom.removeAllViews()
        for (chip in groupChip) {
            chipGroupSymptom.addView(chip)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        uriImage = null
    }
}
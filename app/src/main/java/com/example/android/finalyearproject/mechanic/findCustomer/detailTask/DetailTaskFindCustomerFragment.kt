package com.example.android.finalyearproject.mechanic.findCustomer.detailTask

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.customer.findMech.detailTask.DetailTaskFindMechFragmentArgs
import com.example.android.finalyearproject.databinding.FragmentDetailTaskFindCustomerBinding
import com.example.android.finalyearproject.databinding.FragmentDetailTaskFindMechBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*

class DetailTaskFindCustomerFragment: Fragment(), DownloadImageListener {
    private var _binding: FragmentDetailTaskFindCustomerBinding? = null
    private val binding get() = _binding!!
    private lateinit var args: DetailTaskFindCustomerFragmentArgs

    private lateinit var taskData: Task
    private lateinit var address: TextView
    private lateinit var type: TextView
    private lateinit var brand: TextView
    private lateinit var spec: TextView
    private lateinit var desc: TextView
    private lateinit var symptom: TextView
    private lateinit var date: TextView
    private lateinit var imgPreview: ImageView
    private lateinit var icProfileCustomer: ImageView
    private lateinit var cvImgPreview: CardView
    private lateinit var symptomChipGroup: ChipGroup
    private lateinit var allSymptomChip: List<Chip>
    private lateinit var tvNoImagePreview: TextView

    enum class ChipSymptom(val symptom: List<String>) {
        REFRI(kotlin.collections.listOf("เปิดไม่ติด", "ไม่เย็น", "น้ำแข็งเกาะช่องฟรีซ", "น้ำแข็งเกาะผนังตู้เย็น", "ไฟไม่ติด", "ไม่มีเสียงทำงาน", "ประตูปิดไม่สนิท", "อื่นๆ")),
        FAN(kotlin.collections.listOf("เปิดไม่ติด", "ใบพัดไม่หมุน", "คอพัดลมไม่หมุน", "มีเสียงดัง", "หมุนช้า", "ใบพัดแตก", "ปุ่มเลือกเบอร์กดไม่ได้", "อื่นๆ")),
        AIR(kotlin.collections.listOf("เปิดไม่ติด", "แอร์ไม่เย็น", "เสียงดัง", "น้ำหยด", "มีกลิ่นเหม็น", "ใบพัดไม่สวิง", "อื่นๆ")),
        TV(kotlin.collections.listOf("เปิดไม่ติด", "ภาพกระตุก", "สีเพี้ยน", "ไม่มีเสียง", "ภาพไม่ขึ้น", "สัญญาณรบกวน", "อื่นๆ")),
        WASHMACH(kotlin.collections.listOf("เปิดไม่ติด", "ถังไม่หมุน", "ถังไม่หยุดหมุนตอนเปิดฝา", "น้ำรั่ว", "ไม่ระบายน้ำ", "ผงซักฟอกปนกับน้ำยาปรับผ้านุ่ม", "ไม่ผสมผงซักฟอก", "ถังปั่นไม่หยุด", "อื่นๆ")),
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailTaskFindCustomerBinding.inflate(layoutInflater)
        args = DetailTaskFindCustomerFragmentArgs.fromBundle(
            requireArguments())
        taskData = args.taskData
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentDetailTaskFindCustomerBinding) {
        address = binding.tvdetailFindCustomerAddress
        type = binding.tvdetailFindCustomerType
        brand = binding.tvdetailFindCustomerBrand
        spec = binding.tvdetailFindCustomerSpec
        desc = binding.tvdetailFindCustomerDesc
        symptomChipGroup = binding.symptomChipGroup
        symptom = binding.tvdetailFindCustomerSymptom
        date = binding.tvdetailFindCustomerDate
        imgPreview = binding.imagePreview
        cvImgPreview = binding.cvImagePreview
        tvNoImagePreview = binding.tvDetailFindCustomerNoImagePreview
        icProfileCustomer = binding.imgProfileCustomerDetailFindCustomer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        taskData.chipSymptom?.let { checkSelectedChip(it) }
    }

    private fun initView() {
        type.text = taskData.type
        brand.text = taskData.brand
        spec.text = taskData.spec
        desc.text = taskData.desc
        when (taskData.type) {
            "ตู้เย็น" -> setChipSymptom(ChipSymptom.REFRI.symptom)
            "ทีวี" -> setChipSymptom(ChipSymptom.TV.symptom)
            "พัดลม" -> setChipSymptom(ChipSymptom.FAN.symptom)
            "แอร์" -> setChipSymptom(ChipSymptom.AIR.symptom)
            "เครื่องซักผ้า" -> setChipSymptom(ChipSymptom.WASHMACH.symptom)
        }
        symptom.text = taskData.symptom
        date.text = setDate(taskData.date).joinToString(",  ")
        address.text = taskData.address
        taskData.taskId?.let { DownloadImageUtils.getImageFromStorage(it, this) }
        icProfileCustomer.setOnClickListener { view ->
            view.findNavController().navigate(DetailTaskFindCustomerFragmentDirections
                .actionDetailTaskFindCustomerFragmentToProfileFragment(args.taskData.creatorUId!!))
        }
    }

    private fun setDate(date: List<Date>?): List<String> {
        val result = mutableListOf<String>()
        date?.onEach { result.add(SimpleDateFormat("dd/MM/yyyy").format(it)) }
        return result
    }

    private fun setChipSymptom(symptom: List<String>) {
        val inflator = LayoutInflater.from(symptomChipGroup.context)
        val groupChip = symptom.map { type ->
            val chip = inflator.inflate(R.layout.item_chip_symptom, symptomChipGroup, false) as Chip
            chip.text = type
            chip.tag = type
            chip
        }
        allSymptomChip = groupChip
        symptomChipGroup.removeAllViews()
    }

    private fun checkSelectedChip(symptom: String) {
        val selectedChip = allSymptomChip.filter { it.text.toString() == symptom }
        selectedChip.onEach { it.isChecked = true }
        allSymptomChip.onEach { it.isCheckable = false }
        selectedChip.onEach { symptomChipGroup.addView(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgPreview.setImageBitmap(Bitmap.createScaledBitmap(
                bmp,
                cvImgPreview.width,
                cvImgPreview.height,
                false
            ))
        } else {
            tvNoImagePreview.visibility = View.VISIBLE
        }
    }
}
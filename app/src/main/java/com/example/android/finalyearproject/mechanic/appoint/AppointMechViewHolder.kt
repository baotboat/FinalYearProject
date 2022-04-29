package com.example.android.finalyearproject.mechanic.appoint

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.customer.appoint.AppointFragmentDirections
import com.example.android.finalyearproject.databinding.ItemAppointMechBinding
import com.example.android.finalyearproject.model.Task

class AppointMechViewHolder(
    val binding: ItemAppointMechBinding,
): RecyclerView.ViewHolder(binding.root), DownloadImageListener {

    val cvItemAppointMech = binding.cvItemAppointMech
    val type = binding.tvItemAppointMechType
    val status = binding.tvItemAppointMechStatus
    val imgPreview = binding.imgItemAppointMechPreview
    val customerName = binding.tvItemAppointMechMechName
    val date = binding.tvItemAppointMechDate
    val symptom = binding.tvItemAppointMechSymptom
    private lateinit var electricalApplianceType: String

    companion object {
        fun create(parent: ViewGroup): AppointMechViewHolder {
            val binding = ItemAppointMechBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return AppointMechViewHolder(binding)
        }
    }

    fun bindUI(taskData: Task) {
        val selectedOffer = taskData.listOffer?.find { it.customerSelected == true }
        electricalApplianceType = taskData.type!!
        taskData.taskId?.let { DownloadImageUtils.getImageFromStorage(it, this) }
        type.text = taskData.type
        status.text = taskData.status
        customerName.text = taskData.creatorName
        date.text = selectedOffer?.confirmDate
        symptom.text = taskData.symptom
        cvItemAppointMech.setOnClickListener { view ->
            view.findNavController().navigate(AppointMechFragmentDirections
                .actionNavigationAppointMechToDetailAppointMechFragment(taskData)
            )
        }
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgPreview.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                imgPreview.width,
                imgPreview.height,
                false)
            )
        } else {
            when (electricalApplianceType) {
                "ตู้เย็น" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_refrigerator)
                "ทีวี" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_television)
                "พัดลม" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_fan)
                "แอร์" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_air)
                "เครื่องซักผ้า" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_washing_machine)
            }
        }
    }
}
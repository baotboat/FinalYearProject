package com.example.android.finalyearproject.customer.appoint

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.customer.findMech.detailTask.DetailTaskFindMechFragmentDirections
import com.example.android.finalyearproject.databinding.ItemAppointBinding
import com.example.android.finalyearproject.mechanic.appoint.AppointMechFragment
import com.example.android.finalyearproject.model.Task


class AppointViewHolder(
    val binding: ItemAppointBinding,
    ): RecyclerView.ViewHolder(binding.root), DownloadImageListener {

    val cvItemAppoint = binding.cvItemAppoint
    val type = binding.tvItemAppointType
    val status = binding.tvItemAppointStatus
    val imgPreview = binding.imgItemAppointPreview
    val mechName = binding.tvItemAppointMechName
    val date = binding.tvItemAppointDate
    val symptom = binding.tvItemAppointSymptom
    private lateinit var electricalApplianceType: String

    companion object {
        fun create(parent: ViewGroup): AppointViewHolder {
            val binding = ItemAppointBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return AppointViewHolder(binding)
        }
    }

    fun bindUI(taskData: Task) {
        val selectedOffer = taskData.listOffer?.find { it.customerSelected == true }
        electricalApplianceType = taskData.type!!
        taskData.taskId?.let { DownloadImageUtils.getImageFromStorage(it, this) }
        type.text = taskData.type
        status.text = taskData.status
        mechName.text = selectedOffer?.mechName
        date.text = selectedOffer?.confirmDate
        symptom.text = taskData.symptom
        cvItemAppoint.setOnClickListener { view ->
            view.findNavController().navigate(AppointFragmentDirections
                .actionAppointToDetailAppointFragment(taskData)
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
package com.example.android.finalyearproject.customer.findMech

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.databinding.ItemFindMechTaskBinding
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.model.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class FindMechViewHolder(
    val binding: ItemFindMechTaskBinding,
    val context: Context,
) : RecyclerView.ViewHolder(binding.root), DownloadImageListener {

    val type = binding.tvItemFindMechType
    val status = binding.tvItemFindMechStatus
    val brand = binding.tvItemFindMechBrand
    val symptom = binding.tvItemFindMechSymptom
    val imgPreview = binding.imgItemFindMechPreview
    val imgChooseMech = binding.imgItemFindMechChooseMech
    val imgTaskRef = binding.imgItemFindMechTaskRef
    val cardFindMechTask = binding.cvItemFindCustomer
    val postedDate = binding.tvItemFindMechPostedDate
    private lateinit var typeEletric: String

    companion object {
        fun create(parent: ViewGroup): FindMechViewHolder {
            val binding = ItemFindMechTaskBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return FindMechViewHolder(binding, parent.context)
        }
    }

    fun bind(data: Task) {
        typeEletric = data.type.toString()
        data.taskId?.let { DownloadImageUtils.getImageFromStorage(it, this) }
        type.text = data.type
        status.text = data.status
        brand.text = data.brand
        symptom.text = data.symptom
        postedDate.text = data.taskId?.substring(0, 10)?.replace("-", "/")
        imgChooseMech.setOnClickListener { view: View ->
            data.taskId?.let {
                view.findNavController()
                    .navigate(FindMechFragmentDirections
                        .actionFindMechanicToMechOffer(data.taskId!!))
            }
        }
        cardFindMechTask.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(FindMechFragmentDirections
                    .actionFindMechanicToDetailTaskFindMech(data))
        }
        imgTaskRef.setOnClickListener { view ->
            view.findNavController()
                .navigate(FindMechFragmentDirections
                    .actionFindMechanicToReferTaskFragment(data.type!!, data.chipSymptom!!, data.spec!!))
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
            when (typeEletric) {
                "ตู้เย็น" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_refrigerator)
                "ทีวี" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_television)
                "พัดลม" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_fan)
                "แอร์" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_air)
                "เครื่องซักผ้า" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_washing_machine)
            }
        }
    }
}
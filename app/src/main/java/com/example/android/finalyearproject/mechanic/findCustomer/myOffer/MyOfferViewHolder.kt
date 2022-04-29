package com.example.android.finalyearproject.mechanic.findCustomer.myOffer

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.ItemMyOfferBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.firebase.auth.FirebaseUser

class MyOfferViewHolder(
    val binding: ItemMyOfferBinding,
    val currentUser: FirebaseUser?,
    ) : RecyclerView.ViewHolder(binding.root), DownloadImageListener {

    val name = binding.tvItemMyOfferCustomer
    val type = binding.tvItemMyOfferType
    val brand = binding.tvItemMyOfferBrand
    val symptom = binding.tvItemMyOfferSymptom
    val imgPreview = binding.imgItemMyOfferPreview
    val confirmDate = binding.tvItemMyOfferAppoint
    val priceMin = binding.tvItemMyOfferMinPrice
    val priceMax = binding.tvItemMyOfferMaxPrice
    val btnDismissOffer = binding.btnItemMyOfferDismissOffer
    val btnDeleteOffer = binding.btnItemMyOfferDeleteOffer
    val viewBar = binding.barItemMyOffer
    val tvCustomerDismiss = binding.tvItemMechOfferCustomerDismiss
    val layoutToHide = binding.layoutToHide
    val taskRepository = TaskRepository()
    private lateinit var typeEletric: String

    companion object {
        fun create(parent: ViewGroup, currentUser: FirebaseUser?): MyOfferViewHolder {
            val binding = ItemMyOfferBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return MyOfferViewHolder(binding, currentUser)
        }
    }

    fun bindUiModel(uiModel: Task) {
        typeEletric = uiModel.type.toString()
        if (uiModel.status == "นัดหมายสำเร็จ") {
            btnDismissOffer.visibility = View.INVISIBLE
            btnDeleteOffer.visibility = View.VISIBLE
            val customerSelected = uiModel.listOffer?.find { it.mechUId == currentUser?.uid }?.customerSelected
            if (customerSelected == false) {
                viewBar.setBackgroundResource(R.color.holo_red_light)
                type.setTextColor(Color.parseColor("#ffff4444"))
                tvCustomerDismiss.visibility = View.VISIBLE
                setUi(uiModel)
            } else {
                layoutToHide.visibility = View.GONE
            }
        } else {
            setUi(uiModel)
        }

    }

    private fun setUi(uiModel: Task) {
        uiModel.taskId?.let { DownloadImageUtils.getImageFromStorage(it, this) }
        name.text = uiModel.creatorName
        type.text = uiModel.type
        brand.text = uiModel.brand
        symptom.text = uiModel.symptom
        confirmDate.text = uiModel.listOffer?.find { it.mechUId == currentUser?.uid }?.confirmDate
        priceMin.text = uiModel.listOffer?.find { it.mechUId == currentUser?.uid }?.minPrice.toString()
        priceMax.text = uiModel.listOffer?.find { it.mechUId == currentUser?.uid }?.maxPrice.toString()
        val offer = uiModel.listOffer?.find { it.mechUId == currentUser?.uid }
        btnDismissOffer.setOnClickListener {
            dissmissOffer(uiModel.taskId, offer)
        }
        btnDeleteOffer.setOnClickListener {
            deleteOffer(uiModel.taskId, offer)
        }
    }

    private fun deleteOffer(taskId: String?, offer: Task.Offer?) {
        taskId?.let {
            offer?.let { taskRepository.removeListOfferByTaskId(taskId, offer) }
        }
        binding.layoutToHide.visibility = View.GONE
    }


    private fun dissmissOffer(taskId: String?, offer: Task.Offer?) {
        taskId?.let {
            offer?.let {
                taskRepository.removeWhoSentOfferByTaskId(
                    taskId,
                    Task.Decision(
                        mechUId = currentUser?.uid,
                        decistion = "เสนอราคา"
                    )
                )
                taskRepository.removeListOfferByTaskId(taskId, offer)
            }
        }
        binding.layoutToHide.visibility = View.GONE
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgPreview.setImageBitmap(Bitmap.createScaledBitmap(
                bmp,
                imgPreview.width,
                imgPreview.height,
                false
            ))
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
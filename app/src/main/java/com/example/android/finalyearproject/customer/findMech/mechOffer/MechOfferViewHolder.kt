package com.example.android.finalyearproject.customer.findMech.mechOffer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.ItemMechOfferBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository

class MechOfferViewHolder(
    val binding: ItemMechOfferBinding,
    val context: Context
    ) : RecyclerView.ViewHolder(binding.root), DownloadImageListener {

    val name = binding.tvItemMechOfferCustomer
    val rating = binding.tvItemMechOfferRating
    val date = binding.tvItemMechOfferAppoint
    val minPrice = binding.tvItemMechOfferMinPrice
    val maxPrice = binding.tvItemMechOfferMaxPrice
    val btnComfirmMech = binding.btnItemMechOfferConfirmMech
    val imgPreview = binding.imgItemMechOfferPreview
    val imgLike = binding.imgItemMechOfferLike
    val taskRepo = TaskRepository()

    companion object {
        fun create(parent: ViewGroup): MechOfferViewHolder {
            val binding = ItemMechOfferBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return MechOfferViewHolder(binding, parent.context)
        }
    }

    fun bindUiModel(uiModel: Task.Offer, taskId: String?) {
        uiModel.mechUId?.let { DownloadImageUtils.getImageFromStorage(it, this) }
        name.text = uiModel.mechName
        rating.text = setUIRating(uiModel.mechRating)
        date.text = uiModel.confirmDate
        minPrice.text = uiModel.minPrice.toString()
        maxPrice.text = uiModel.maxPrice.toString()
        name.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        if (uiModel.mechDidSimilarTask == true) {
            imgLike.bringToFront()
            imgLike.visibility = View.VISIBLE
        }
        btnComfirmMech.setOnClickListener { view ->
            taskId?.let {
                taskRepo.updateStatusTask(taskId, "นัดหมายสำเร็จ")
                val updateOffer = uiModel.copy(customerSelected = true)
                taskRepo.updateCustomerSelectedTask(taskId, uiModel, updateOffer)
            }
            view.findNavController()
                .navigate(R.id.action_mechOfferFragment_to_navigation_find_mechanic)
            Toast.makeText(context, "นัดหมายสำเร็จ สามารถดูรายละเอียดได้\n                       " +
                    "ที่หน้า Appoint", Toast.LENGTH_LONG).show()
        }
        name.setOnClickListener { view ->
            uiModel.mechUId?.let {
                Navigation.findNavController(view).navigate(MechOfferFragmentDirections
                    .actionMechOfferFragmentToProfileMechFragment(uiModel.mechUId))
            }

        }
    }

    private fun setUIRating(rating: Float?): String {
        val userRating = rating.toString()
        val ratingUI = when (userRating) {
            "null" ->  "-"
            else -> "$userRating %"
        }
        return ratingUI
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgPreview.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                imgPreview.width,
                imgPreview.height,
                false)
            )
        } else {
            imgPreview.setImageResource(R.drawable.ic_profile_mech_preview)
        }
    }
}
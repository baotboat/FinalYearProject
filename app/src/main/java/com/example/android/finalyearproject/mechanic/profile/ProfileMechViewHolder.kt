package com.example.android.finalyearproject.mechanic.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.databinding.ItemAppointMechBinding
import com.example.android.finalyearproject.databinding.ItemProfileMechHistoryTaskBinding
import com.example.android.finalyearproject.mechanic.appoint.AppointMechViewHolder
import com.example.android.finalyearproject.model.Task

class ProfileMechViewHolder(val binding: ItemProfileMechHistoryTaskBinding)
    : RecyclerView.ViewHolder(binding.root) {

    val customerName = binding.tvItemProfileMechCustomer
    val type = binding.tvItemProfileMechType
    val brand = binding.tvItemProfileMechBrand
    val symptom = binding.tvItemProfileMechSymptom

    companion object {
        fun create(parent: ViewGroup): ProfileMechViewHolder {
            val binding = ItemProfileMechHistoryTaskBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return ProfileMechViewHolder(binding)
        }
    }

    fun bindUI(taskData: Task) {
        customerName.text = taskData.creatorName
        type.text = taskData.type
        brand.text = taskData.brand
        symptom.text = taskData.symptom
    }

}
package com.example.android.finalyearproject.customer.findMech.referTask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.databinding.ItemReferenceTaskBinding
import com.example.android.finalyearproject.model.Task

class ReferTaskViewHolder(
    val binding: ItemReferenceTaskBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val type = binding.tvItemReferTaskType
    val brand = binding.tvItemReferTaskBrand
    val spec = binding.tvItemReferTaskSpec
    val desc = binding.tvItemReferTaskDesc
    val symptom = binding.tvItemReferTaskSymptom
    val price = binding.tvItemReferTaskPrice
    val date = binding.tvItemReferTaskDate
    val mechName = binding.tvItemReferTaskMechName

    companion object {
        fun create(parent: ViewGroup): ReferTaskViewHolder {
            val binding = ItemReferenceTaskBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return ReferTaskViewHolder(binding)
        }
    }

    fun bindUI(taskData: Task) {
        type.text = taskData.type
        brand.text = taskData.brand
        spec.text = taskData.spec
        desc.text = taskData.desc
        symptom.text = taskData.symptom
        price.text = taskData.confirmPrice
        date.text = taskData.listOffer?.find { it.customerSelected == true }?.confirmDate
        mechName.text = taskData.listOffer?.find { it.customerSelected == true }?.mechName
    }
}
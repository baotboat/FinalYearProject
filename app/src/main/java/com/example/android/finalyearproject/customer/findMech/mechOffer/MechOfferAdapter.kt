package com.example.android.finalyearproject.customer.findMech.mechOffer

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.model.Task

class MechOfferAdapter(
    var taskId: String? = null
    ): RecyclerView.Adapter<MechOfferViewHolder>() {

    private var listDiffer = AsyncListDiffer(this, diffUtilItem())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MechOfferViewHolder {
        return MechOfferViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MechOfferViewHolder, position: Int) {
        holder.bindUiModel(listDiffer.currentList.get(position), taskId)
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun setData(newTaskList: List<Task.Offer>) = listDiffer.submitList(newTaskList)

    fun diffUtilItem(): DiffUtil.ItemCallback<Task.Offer> {
        val diffUtil = object : DiffUtil.ItemCallback<Task.Offer>() {
            override fun areItemsTheSame(oldItem: Task.Offer, newItem: Task.Offer): Boolean {
                return oldItem.mechUId == newItem.mechUId
            }

            override fun areContentsTheSame(oldItem: Task.Offer, newItem: Task.Offer): Boolean {
                return oldItem == newItem
            }
        }
        return diffUtil
    }
}
package com.example.android.finalyearproject.mechanic.findCustomer.myOffer

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.customer.findMech.FindMechViewHolder
import com.example.android.finalyearproject.model.Task
import com.google.firebase.auth.FirebaseUser

class MyOfferAdapter(val currentUser: FirebaseUser? = null)
    : RecyclerView.Adapter<MyOfferViewHolder>() {

    private var listDiffer = AsyncListDiffer(this, diffUtilItem())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOfferViewHolder {
        return MyOfferViewHolder.create(parent, currentUser)
    }

    override fun onBindViewHolder(holder: MyOfferViewHolder, position: Int) {
        holder.bindUiModel(listDiffer.currentList.get(position))
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun setData(newTaskList: List<Task>) = listDiffer.submitList(newTaskList)

    fun diffUtilItem(): DiffUtil.ItemCallback<Task> {
        val diffUtil = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.taskId == newItem.taskId
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
        return diffUtil
    }
}
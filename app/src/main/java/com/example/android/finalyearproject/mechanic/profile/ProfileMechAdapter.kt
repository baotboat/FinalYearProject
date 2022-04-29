package com.example.android.finalyearproject.mechanic.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.mechanic.appoint.AppointMechViewHolder
import com.example.android.finalyearproject.model.Task

class ProfileMechAdapter: RecyclerView.Adapter<ProfileMechViewHolder>() {

    private var listDiffer = AsyncListDiffer(this, diffUtilItem())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMechViewHolder {
        return ProfileMechViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProfileMechViewHolder, position: Int) {
        holder.bindUI(listDiffer.currentList[position])
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
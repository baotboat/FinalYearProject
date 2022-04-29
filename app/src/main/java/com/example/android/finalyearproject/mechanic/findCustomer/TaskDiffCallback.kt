package com.example.android.finalyearproject.mechanic.findCustomer

import androidx.recyclerview.widget.DiffUtil
import com.example.android.finalyearproject.model.Task

class TaskDiffCallback (
    private val oldList: List<Task>,
    private val newList: List<Task>
    ): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition).taskId == newList[newItemPosition].taskId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}
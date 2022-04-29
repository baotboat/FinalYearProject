package com.example.android.finalyearproject.customer.findMech

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task

class FindMechAdapter(private val taskList: List<Task>): RecyclerView.Adapter<FindMechViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindMechViewHolder {
        return FindMechViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FindMechViewHolder, position: Int) {
        taskList.get(position).let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
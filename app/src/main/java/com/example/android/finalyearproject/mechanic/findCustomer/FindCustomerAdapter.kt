package com.example.android.finalyearproject.mechanic.findCustomer

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskRepository

class FindCustomerAdapter(
    private val fragmentManager: FragmentManager,
    private val findCustomerListener: FindCustomerFragmentListener,
    private val taskRepository: TaskRepository
): RecyclerView.Adapter<FindCustomerViewHolder>() {

    private var listDiffer = AsyncListDiffer(this, diffUtilItem())
    private var currentUserData = User()

    companion object {
        const val FILTER = 1
        const val TASK = 2
    }

    override fun getItemViewType(position: Int): Int {
        return  when(position) {
            0 -> FILTER
            else -> TASK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindCustomerViewHolder {
       return when (viewType) {
           FILTER -> FindCustomerFilterViewHolder.create(parent, fragmentManager, findCustomerListener)
           TASK -> FindCustomerTaskViewHolder.create(parent, currentUserData, findCustomerListener, taskRepository)
           else -> FindCustomerTaskViewHolder.create(parent, currentUserData, findCustomerListener, taskRepository)
       }
    }

    override fun onBindViewHolder(holder: FindCustomerViewHolder, position: Int) {
        when (holder) {
            is FindCustomerFilterViewHolder -> holder.bindUiModel(listDiffer.currentList.get(position))
            is FindCustomerTaskViewHolder -> {
                holder.initView(listDiffer.currentList[position])
                holder.bindUiModel(listDiffer.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun setCurrentUserData(userData: User) {
        currentUserData = userData
    }

//    fun setListTaskData(newTaskList: List<Task>) = listDiffer.submitList(newTaskList)

    //Use this one before fixing cause Adapter don't recreate old item in listDiffer
    // so can't compare limitDistance when filter distance
    fun setAllNewTaskListData(newTaskList: List<Task>) {
        listDiffer.submitList(null)
        listDiffer.submitList(newTaskList)
    }

    private fun diffUtilItem(): DiffUtil.ItemCallback<Task> {
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
//    var taskList =  listOf<Task>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

//    fun setData(newTaskList: List<Task>) {
//        if (taskList == null) {
//            taskList = newTaskList
//            notifyDataSetChanged()
//        } else {
//            Log.e("setData", "setData")
//            val diffUtil = taskList?.let { TaskDiffCallback(it, newTaskList) }
//            val diffResults = diffUtil?.let { DiffUtil.calculateDiff(it) }
//            diffResults?.dispatchUpdatesTo(this)
//            taskList = newTaskList
//        }
//    }






package com.example.android.finalyearproject.mechanic.findCustomer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.ItemFindCustomerFilterBinding
import com.example.android.finalyearproject.databinding.ItemFindCustomerTaskBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

abstract class FindCustomerViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

        abstract fun bindUiModel(uiModel: Task)
}
package com.example.android.finalyearproject.customer.appoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.customer.findMech.FindMechAdapter
import com.example.android.finalyearproject.databinding.FragmentAppointBinding
import com.example.android.finalyearproject.databinding.FragmentFindMechBinding
import com.example.android.finalyearproject.mechanic.findCustomer.FindCustomerAdapter
import com.example.android.finalyearproject.repository.TaskRepository

class AppointFragment : Fragment() {

    private var _binding: FragmentAppointBinding? = null
    private val binding get() = _binding!!
    private lateinit var appointViewModel: AppointViewModel
    private lateinit var appointViewModelFactory: AppointViewModelFactory

    private lateinit var rcvAppoint: RecyclerView
    private lateinit var appointAdapter: AppointAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointBinding.inflate(inflater, container, false)
        val taskRepo = TaskRepository()
        appointViewModelFactory = AppointViewModelFactory(taskRepo)
        appointViewModel =
            ViewModelProvider(this, appointViewModelFactory).get(AppointViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentAppointBinding) {
        rcvAppoint = binding.rcvAppoint
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        appointViewModel.getTasksByUId()
    }

    private fun initView() {
        appointAdapter = AppointAdapter()
        rcvAppoint.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appointAdapter
        }
    }

    private fun initObserve() {
        appointViewModel.taskListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                appointAdapter.setData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
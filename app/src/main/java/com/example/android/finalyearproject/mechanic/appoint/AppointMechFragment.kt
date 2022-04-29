package com.example.android.finalyearproject.mechanic.appoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.customer.appoint.AppointAdapter
import com.example.android.finalyearproject.customer.appoint.AppointViewModel
import com.example.android.finalyearproject.customer.appoint.AppointViewModelFactory
import com.example.android.finalyearproject.databinding.FragmentAppointBinding
import com.example.android.finalyearproject.databinding.FragmentAppointMechBinding
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AppointMechFragment: Fragment() {

    private var _binding: FragmentAppointMechBinding? = null
    private val binding get() = _binding!!
    private lateinit var appointMechViewModel: AppointMechViewModel
    private lateinit var appointMechViewModelFactory: AppointMechViewModelFactory

    private lateinit var rcvAppointMech: RecyclerView
    private lateinit var appointMechAdapter: AppointMechAdapter

    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointMechBinding.inflate(inflater, container, false)

        val taskRepo = TaskRepository()
        appointMechViewModelFactory = AppointMechViewModelFactory(taskRepo)
        appointMechViewModel =
            ViewModelProvider(this, appointMechViewModelFactory)
                .get(AppointMechViewModel::class.java)
        currentUser = FirebaseAuth.getInstance().currentUser
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentAppointMechBinding) {
        rcvAppointMech = binding.rcvAppointMech
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        appointMechViewModel.getTasksByStatus(currentUser!!.uid)
    }

    private fun initView() {
        appointMechAdapter = AppointMechAdapter()
        rcvAppointMech.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appointMechAdapter
        }
    }

    private fun initObserve() {
        appointMechViewModel.taskListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                appointMechAdapter.setData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentUser = null
    }
}
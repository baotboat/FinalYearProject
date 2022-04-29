package com.example.android.finalyearproject.customer.findMech

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.FragmentFindMechBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FindMechFragment : Fragment() {

    private var _binding: FragmentFindMechBinding? = null
    private val binding get() = _binding!!
    private lateinit var findMechViewModel: FindMechViewModel
    private lateinit var findMechviewModelFactory: FindMechViewModelFactory

    private lateinit var fabFindMech: FloatingActionButton
    private lateinit var rcvFindMech: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindMechBinding.inflate(layoutInflater)
        val repo = TaskRepository()
        findMechviewModelFactory = FindMechViewModelFactory(repo)
        findMechViewModel = ViewModelProvider(this, findMechviewModelFactory)
                .get(FindMechViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentFindMechBinding) {
        fabFindMech = binding.fabFindMechanic
        rcvFindMech = binding.rcvFindMech
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        findMechViewModel.getTasksByUId()
    }

    private fun initView() {
        rcvFindMech.apply {
            layoutManager = LinearLayoutManager(context)
        }
        fabFindMech.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_navigation_find_mechanic_to_createTaskFragment)
        }
    }

    private fun initObserve() {
        findMechViewModel.taskListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                rcvFindMech.adapter = FindMechAdapter(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
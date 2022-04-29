package com.example.android.finalyearproject.customer.findMech.referTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.customer.findMech.mechOffer.MechOfferAdapter
import com.example.android.finalyearproject.customer.findMech.mechOffer.MechOfferFragmentArgs
import com.example.android.finalyearproject.customer.findMech.mechOffer.MechOfferViewModel
import com.example.android.finalyearproject.customer.findMech.mechOffer.MechOfferViewModelFactory
import com.example.android.finalyearproject.databinding.FragmentMechOfferBinding
import com.example.android.finalyearproject.databinding.FragmentReferenceTaskBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository

class ReferTaskFragment: Fragment() {
    private var _binding: FragmentReferenceTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var referTaskViewModel: ReferTaskViewModel
    private lateinit var referTaskViewModelFactory: ReferTaskViewModelFactory

    private lateinit var referTaskAdapter: ReferTaskAdapter
    private lateinit var rcvReferTask: RecyclerView
    private lateinit var args: ReferTaskFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        args = ReferTaskFragmentArgs.fromBundle(requireArguments())
        _binding = FragmentReferenceTaskBinding.inflate(layoutInflater)
        val repo = TaskRepository()
        referTaskViewModelFactory = ReferTaskViewModelFactory(repo)
        referTaskViewModel = ViewModelProvider(this, referTaskViewModelFactory)
            .get(ReferTaskViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentReferenceTaskBinding) {
        rcvReferTask = binding.rcvReferenceTask
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        referTaskViewModel.getTasksByChipSymptom(args.chipSymptom, args.type, args.spec)
    }

    private fun initView() {
        referTaskAdapter = ReferTaskAdapter()
        rcvReferTask.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = referTaskAdapter
        }
    }

    private fun initObserve() {
        referTaskViewModel.taskListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                referTaskAdapter.setData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
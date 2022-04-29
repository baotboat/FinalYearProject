package com.example.android.finalyearproject.customer.findMech.mechOffer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.databinding.FragmentMechOfferBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository

class MechOfferFragment : Fragment() {

    private var _binding: FragmentMechOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var mechOfferViewModel: MechOfferViewModel
    private lateinit var mechOfferViewModelFactory: MechOfferViewModelFactory

    private lateinit var mechOfferAdapter: MechOfferAdapter
    private lateinit var rcvMechOffer: RecyclerView
    private lateinit var arg: MechOfferFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        arg = MechOfferFragmentArgs.fromBundle(requireArguments())
        _binding = FragmentMechOfferBinding.inflate(layoutInflater)
        val repo = TaskRepository()
        mechOfferViewModelFactory = MechOfferViewModelFactory(repo)
        mechOfferViewModel = ViewModelProvider(this, mechOfferViewModelFactory)
            .get(MechOfferViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentMechOfferBinding) {
        rcvMechOffer = binding.rcvMechOffer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        mechOfferViewModel.getTasksBytaskId(arg.taskId)
    }

    private fun initView() {
        mechOfferAdapter = MechOfferAdapter()
        rcvMechOffer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mechOfferAdapter
        }
    }

    private fun initObserve() {
        mechOfferViewModel.response.observe(viewLifecycleOwner) { it ->
            it.taskList?.let {
                val listMechOffer = mutableListOf<Task.Offer>()
                var task = it[0]
                mechOfferAdapter.taskId = task.taskId
                task.listOffer?.sortedByDescending { it.mechRating }.also {
                    it?.onEach {
                        listMechOffer.add(it)
                    }
                }
                mechOfferAdapter.setData(listMechOffer)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
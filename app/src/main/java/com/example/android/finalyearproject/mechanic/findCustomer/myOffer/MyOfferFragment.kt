package com.example.android.finalyearproject.mechanic.findCustomer.myOffer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.databinding.FragmentFindCustomerBinding
import com.example.android.finalyearproject.databinding.FragmentMyOfferBinding
import com.example.android.finalyearproject.mechanic.findCustomer.FindCustomerAdapter
import com.example.android.finalyearproject.mechanic.findCustomer.FindCustomerViewModel
import com.example.android.finalyearproject.mechanic.findCustomer.FindCustomerViewModelFactory
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MyOfferFragment: Fragment() {

    private var _binding: FragmentMyOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var myOfferViewModel: MyOfferViewModel
    private lateinit var myOfferViewModelFactory: MyOfferViewModelFactory
    private lateinit var myOfferAdapter: MyOfferAdapter

    private lateinit var rcvMyOffer: RecyclerView
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyOfferBinding.inflate(inflater, container, false)
        val repo = TaskRepository()
        myOfferViewModelFactory = MyOfferViewModelFactory(repo)
        myOfferViewModel = ViewModelProvider(this, myOfferViewModelFactory)
            .get(MyOfferViewModel::class.java)
        findView(binding)
        currentUser = FirebaseAuth.getInstance().currentUser

        return binding.root
    }

    private fun findView(binding: FragmentMyOfferBinding) {
        rcvMyOffer = binding.rcvMyOffer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        myOfferViewModel.getTask()
    }

    private fun initView() {
        myOfferAdapter = MyOfferAdapter(currentUser)
        rcvMyOffer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myOfferAdapter
        }
    }

    private fun initObserve() {
        myOfferViewModel.response.observe(viewLifecycleOwner) { it ->
            it.taskList?.let {
                updateTask(it)
            }
        }
    }

    private fun updateTask(taskList: List<Task>) {
        val taskListForUi = taskList.reversed().filter {
            it.listWhoSentOffer?.contains(Task.Decision(
                mechUId = currentUser?.uid,
                decistion = "เสนอราคา"
            )) == true &&
            it.status == "กำลังหาช่าง" || it.status == "นัดหมายสำเร็จ"
        }
        myOfferAdapter.setData(taskListForUi)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
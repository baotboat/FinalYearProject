package com.example.android.finalyearproject.mechanic.findCustomer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android.finalyearproject.databinding.FragmentFindCustomerBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.maps.android.SphericalUtil

class FindCustomerFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    FindCustomerFragmentListener {

    private var _binding: FragmentFindCustomerBinding? = null
    private val binding get() = _binding!!
    private lateinit var findCustomerViewModel: FindCustomerViewModel
    private lateinit var findCustomerViewModelFactory: FindCustomerViewModelFactory
    private lateinit var rcvFindCustomer: RecyclerView
    private lateinit var findCustomerAdapter: FindCustomerAdapter
    private lateinit var srLayout: SwipeRefreshLayout

    private var currentUser: FirebaseUser? = null
    private var filterTypeSelected: List<String>? = null
    private var filterLimitDistance: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFindCustomerBinding.inflate(inflater, container, false)
        val taskRepo = TaskRepository()
        val userRepo = UserRepository()
        findCustomerViewModelFactory = FindCustomerViewModelFactory(taskRepo, userRepo)
        findCustomerViewModel = ViewModelProvider(this, findCustomerViewModelFactory)
            .get(FindCustomerViewModel::class.java)
        findView(binding)
        currentUser = FirebaseAuth.getInstance().currentUser

        return binding.root
    }

    private fun findView(binding: FragmentFindCustomerBinding) {
        rcvFindCustomer = binding.rcvFindCustomer
        srLayout = binding.srLayoutFindCustomer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        getUserData()
        getTask()
    }

    private fun initView() {
        findCustomerAdapter = FindCustomerAdapter(
            parentFragmentManager,
            this@FindCustomerFragment,
            TaskRepository()
        )
        rcvFindCustomer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = findCustomerAdapter
        }
        srLayout.setOnRefreshListener(this)
    }

    private fun initObserve() {
        findCustomerViewModel.taskListLiveData.observe(viewLifecycleOwner) { it ->
            it?.let { updateTask(it) }
        }
        findCustomerViewModel.getUserData.observe(viewLifecycleOwner) {
            findCustomerAdapter.setCurrentUserData(it)
        }
    }

    private fun getUserData() = currentUser?.let { findCustomerViewModel.getUserByUId(it.uid) }

    private fun getTask(filterTypeSelected: List<String>? = null) =
        findCustomerViewModel.getTasksByFilters(filterTypeSelected, currentUser!!.uid)

    private fun updateTask(taskList: List<Task>) {
        findCustomerAdapter.setAllNewTaskListData(taskList)
        rcvFindCustomer.smoothScrollToPosition(rcvFindCustomer.top)
    }

    override fun onRefresh() {
        getTask(filterTypeSelected)
        srLayout.isRefreshing = false
    }

    override fun getSelectedFilter(
        filterTypeSelected: List<String>,
        filterDistanceSelected: Float?,
    ) {
        filterLimitDistance = filterDistanceSelected
        if (filterTypeSelected.isNullOrEmpty()) {
            this.filterTypeSelected = null
            getTask()
        } else {
            this.filterTypeSelected = filterTypeSelected
            getTask(filterTypeSelected)
        }
    }

    override fun computeDistanceFromLatLng(
        view: View,
        tvDistance: TextView,
        myLatLng: LatLng?,
        customerLatLng: LatLng?,
    ) {
        view.visibility = View.VISIBLE
        view.layoutParams.height = -2
        if (myLatLng != null && customerLatLng != null) {
            val distance = SphericalUtil.computeDistanceBetween(myLatLng, customerLatLng) / 1000
            tvDistance.text = String.format("%.2f", distance)
            checkLimitDistance(distance, view)
        } else {
            tvDistance.text = "-"
        }
    }

    private fun checkLimitDistance(distance: Double, view: View) {
        filterLimitDistance?.let {
            if (distance > filterLimitDistance!!) {
                view.layoutParams.height = 0
                view.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        filterTypeSelected = null
        currentUser = null
        filterLimitDistance = null
    }
}
//fun View.delayInLifecycle(
//    durationInMillis: Long,
//    dispatcher: CoroutineDispatcher = Dispatchers.Main,
//    block: () -> Unit,
//): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
//    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
//        delay(durationInMillis)
//        block()
//    }
//}

//        findCustomerViewModel.getUserByUId(currentUser!!.uid).observe(viewLifecycleOwner) {
//            findCustomerAdapter.setCurrentUserData(it)
//        }
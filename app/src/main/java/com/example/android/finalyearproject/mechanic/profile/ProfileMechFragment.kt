package com.example.android.finalyearproject.mechanic.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.finalyearproject.customer.profile.changeProfile.ProfileFragmentListener
import com.example.android.finalyearproject.databinding.FragmentProfileMechBinding
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.example.android.finalyearproject.sign.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileMechFragment : Fragment(), ProfileFragmentListener {

    private lateinit var args: ProfileMechFragmentArgs
    private var _binding: FragmentProfileMechBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileMechViewModel: ProfileMechViewmodel
    private lateinit var profileMechViewModelFactory: ProfileMechViewmodelFactory

    private lateinit var btnLogoutProfileMech: Button
    private lateinit var btnChangeProfileMech: Button
    private lateinit var nameProfileMech: TextView
    private lateinit var ratingProfileMech: TextView
    private lateinit var typeProfileMech: TextView
    private lateinit var descProfileMech: TextView
    private lateinit var tvProfileMechAddress: TextView
    private lateinit var tvProfileMechPhone: TextView
    private lateinit var tvSucceedTaskProfileMech: TextView
    private lateinit var tvFailedTaskProfileMech: TextView
    private lateinit var userData: User
    private lateinit var imgProfile: ImageView
    private lateinit var imgProfileLoading: ImageView
    private lateinit var userUId: String
    private lateinit var rcvTaskHistory: RecyclerView
    private lateinit var profileMechAdapter: ProfileMechAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileMechBinding.inflate(inflater, container, false)
        val userRepo = UserRepository()
        val taskRepo = TaskRepository()
        profileMechViewModelFactory = ProfileMechViewmodelFactory(userRepo, taskRepo)
        profileMechViewModel = ViewModelProvider(this, profileMechViewModelFactory)
            .get(ProfileMechViewmodel::class.java)
        findView(binding)
        if (arguments != null) {
            args = ProfileMechFragmentArgs.fromBundle(requireArguments())
            userUId = args.uId!!
            btnChangeProfileMech.visibility = View.GONE
            btnLogoutProfileMech.visibility = View.GONE
        } else {
            userUId = FirebaseAuth.getInstance().currentUser!!.uid
        }

        return binding.root
    }

    private fun findView(binding: FragmentProfileMechBinding) {
        btnLogoutProfileMech = binding.btnProfileMechLogout
        nameProfileMech = binding.tvProfileMechName
        ratingProfileMech = binding.tvProfileMechRating
        typeProfileMech = binding.tvProfileMechType
        descProfileMech = binding.tvProfileMechDesc
        btnChangeProfileMech = binding.btnProfileMechChangeProfile
        imgProfile = binding.imgProfileMech
        imgProfileLoading = binding.imgProfileMechLoading
        tvProfileMechAddress = binding.tvProfileMechAddress
        tvProfileMechPhone = binding.tvProfileMechPhone
        tvSucceedTaskProfileMech = binding.tvProfileMechSucceedTask
        tvFailedTaskProfileMech = binding.tvProfileMechFailedTask
        rcvTaskHistory = binding.rcvProfileMechTaskHistory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        profileMechViewModel.getUser(userUId)
        imgProfile.visibility = View.INVISIBLE
        imgProfileLoading.visibility = View.VISIBLE
        profileMechViewModel.loadImage(userUId)
        profileMechViewModel.getTasksByStatus(userUId)
    }

    private fun initView() {
        profileMechAdapter = ProfileMechAdapter()
        rcvTaskHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileMechAdapter
        }
        btnLogoutProfileMech.setOnClickListener {
            val intent = Intent(context, SignInActivity::class.java)
            startActivity(intent)
        }
        btnChangeProfileMech.setOnClickListener {
            DialogChangeProfileMech(userData, this@ProfileMechFragment).show(childFragmentManager,
                "DialogChangeProfileMech")
        }
    }

    private fun initObserve() {
        profileMechViewModel.getUser.observe(viewLifecycleOwner) {
            userData = it
            bindUserData(it)
        }
        profileMechViewModel.changeUserData.observe(viewLifecycleOwner) {
            getUserData(userUId)
        }
        profileMechViewModel.loadProfileImageCompleted.observe(viewLifecycleOwner) {
            it?.let {
                displayImage(it)
            }
        }
        profileMechViewModel.loadProfileImageFailed.observe(viewLifecycleOwner) {
            imgProfile.visibility = View.VISIBLE
            imgProfileLoading.visibility = View.INVISIBLE
        }
        profileMechViewModel.taskListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                profileMechAdapter.setData(it)
            }
        }
    }

    private fun displayImage(byteArray: ByteArray) {
        var bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imgProfile.setImageBitmap(Bitmap.createScaledBitmap(
            bmp,
            imgProfile.width,
            imgProfile.height,
            false))
        imgProfileLoading.visibility = View.INVISIBLE
        imgProfile.visibility = View.VISIBLE
    }

    private fun getUserData(uId: String) {
        profileMechViewModel.getUser(uId)
    }

    private fun bindUserData(user: User) {
        nameProfileMech.text = user.name + " " + user.surname
        ratingProfileMech.text = setUIRating(user.rating)
        typeProfileMech.text = setUIType(user.type)
        descProfileMech.text = user.desc
        tvProfileMechAddress.text = userData.address
        tvProfileMechPhone.text = user.phone
        setUIAmountTask(user.succeedTask, user.failedTask)
    }

    private fun setUIRating(rating: Float?): String {
        val ratingUI = when (rating.toString()) {
            "null" -> "-"
            else -> String.format("%.2f", rating) + " %"
        }
        return ratingUI
    }

    private fun setUIAmountTask(succeedTask: Int?, failedTask: Int?) {
        if (succeedTask != null) {
            tvSucceedTaskProfileMech.text = succeedTask.toString()
        } else tvSucceedTaskProfileMech.text = "0"
        if (failedTask != null) {
            tvFailedTaskProfileMech.text = failedTask.toString()
        } else tvFailedTaskProfileMech.text = "0"
    }

    private fun setUIType(type: List<String>?): String {
        if (type.isNullOrEmpty()) return "-"
        else return type.joinToString(", ")
    }

    override fun changeUserData(
        desc: String,
        address: String,
        phone: String,
        type: List<String>?,
    ) {
        nameProfileMech.text = ""
        ratingProfileMech.text = ""
        typeProfileMech.text = ""
        descProfileMech.text = ""
        tvProfileMechAddress.text = ""
        tvProfileMechPhone.text = ""
        profileMechViewModel.changeUserData(userUId, desc, type, address, phone)
    }

    override fun changeProfileImage(uri: Uri) {
        imgProfile.visibility = View.INVISIBLE
        imgProfileLoading.visibility = View.VISIBLE
        profileMechViewModel.uploadImage(uri, userUId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
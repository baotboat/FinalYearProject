package com.example.android.finalyearproject.customer.profile

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
import com.example.android.finalyearproject.customer.profile.changeProfile.DialogChangeProfile
import com.example.android.finalyearproject.customer.profile.changeProfile.ProfileFragmentListener
import com.example.android.finalyearproject.databinding.FragmentProfileBinding
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository
import com.example.android.finalyearproject.sign.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(), ProfileFragmentListener{

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileViewModelFactory: ProfileViewModelFactory

    private lateinit var btnLogoutProfile: Button
    private lateinit var btnChangeProfile: Button
    private lateinit var nameProfile: TextView
    private lateinit var ratingProfile: TextView
    private lateinit var addressProfile: TextView
    private lateinit var phoneProfile: TextView
    private lateinit var tvSucceedTaskProfile: TextView
    private lateinit var tvFailedTaskProfile: TextView
    private lateinit var userData: User
    private lateinit var imgProfile: ImageView
    private lateinit var imgProfileLoading: ImageView
    private lateinit var userUId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        var userRepo = UserRepository()
        profileViewModelFactory = ProfileViewModelFactory(userRepo)
        profileViewModel = ViewModelProvider(this, profileViewModelFactory)
                .get(ProfileViewModel::class.java)
        findView(binding)
        if (arguments != null) {
            userUId = ProfileFragmentArgs.fromBundle(requireArguments()).uId!!
            btnChangeProfile.visibility = View.GONE
            btnLogoutProfile.visibility = View.GONE
        } else {
            userUId = FirebaseAuth.getInstance().currentUser!!.uid
        }

        return binding.root
    }

    private fun findView(binding: FragmentProfileBinding) {
        btnLogoutProfile = binding.btnProfileCustomerLogout
        nameProfile = binding.tvProfileCustomerName
        ratingProfile = binding.tvProfileCustomerRating
        btnChangeProfile = binding.btnProfileCustomerChangeProfile
        imgProfile = binding.imgProfileCustomer
        imgProfileLoading = binding.imgProfileCustomerLoading
        addressProfile = binding.tvProfileCustomerAddress
        phoneProfile = binding.tvProfileCustomerPhone
        tvSucceedTaskProfile = binding.tvProfileCustomerSucceedTask
        tvFailedTaskProfile = binding.tvProfileCustomerFailedTask
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        getUserData(userUId)
        imgProfile.visibility = View.INVISIBLE
        imgProfileLoading.visibility = View.VISIBLE
        profileViewModel.loadImage(userUId)
    }

    private fun initView() {
        btnLogoutProfile.setOnClickListener {
            val intent= Intent(context, SignInActivity::class.java)
            startActivity(intent)
        }
        btnChangeProfile.setOnClickListener {
            DialogChangeProfile(userData, this@ProfileFragment)
                .show(childFragmentManager, "DialogChangeProfile")
        }
    }

    private fun initObserve() {
        profileViewModel.getUser.observe(viewLifecycleOwner) {
            userData = it
            bindUserData(it)
        }
        profileViewModel.changeUserData.observe(viewLifecycleOwner) {
            getUserData(userUId)
        }
        profileViewModel.loadProfileImageCompleted.observe(viewLifecycleOwner) {
            it?.let {
                displayImage(it)
            }
        }
        profileViewModel.loadProfileImageFailed.observe(viewLifecycleOwner) {
            imgProfile.visibility = View.VISIBLE
            imgProfileLoading.visibility = View.INVISIBLE
        }
    }

    private fun displayImage(byteArray: ByteArray) {
        var bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imgProfile.setImageBitmap(Bitmap.createScaledBitmap(
            bmp,
            imgProfile.width,
            imgProfile.height,
            false))
        imgProfile.visibility = View.VISIBLE
        imgProfileLoading.visibility = View.INVISIBLE
    }

    private fun getUserData(uId: String) {
        profileViewModel.getUser(uId)
    }

    private fun bindUserData(user: User) {
        nameProfile.text = user.name + " " + user.surname
        ratingProfile.text = setUIRating(user.rating)
        addressProfile.text = user.address
        phoneProfile.text = user.phone
        setUIAmountTask(user.succeedTask, user.failedTask)
    }

    private fun setUIRating(rating: Float?): String {
        val ratingUI = when (rating.toString()) {
            "null" ->  "-"
            else -> String.format("%.2f", rating) + " %"
        }
        return ratingUI
    }

    private fun setUIAmountTask(succeedTask: Int?, failedTask: Int?) {
        if (succeedTask != null) {
            tvSucceedTaskProfile.text = succeedTask.toString()
        } else tvSucceedTaskProfile.text = "0"
        if (failedTask != null) {
            tvFailedTaskProfile.text = failedTask.toString()
        } else tvFailedTaskProfile.text = "0"
    }

    override fun changeUserData(desc: String, address: String, phone:String, type: List<String>?) {
        nameProfile.text = ""
        ratingProfile.text = ""
        addressProfile.text = ""
        phoneProfile.text = ""
        profileViewModel.changeUserData(userUId, desc, address, phone)
    }

    override fun changeProfileImage(uri: Uri) {
        imgProfile.visibility = View.INVISIBLE
        imgProfileLoading.visibility = View.VISIBLE
        profileViewModel.uploadImage(uri, userUId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
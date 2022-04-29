package com.example.android.finalyearproject.mechanic.appoint.rating

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.FragmentRatingMechBinding
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository

class RatingMechFragment : Fragment(), DownloadImageListener, SeekBar.OnSeekBarChangeListener {

    private var _binding: FragmentRatingMechBinding? = null
    private val binding get() = _binding!!
    private lateinit var ratingMechViewModel: RatingMechViewModel
    private lateinit var ratingMechViewModelFactory: RatingMechViewModelFactory

    private lateinit var customerUId: String
    private lateinit var imgProfile: ImageView
    private lateinit var nameCustomer: TextView
    private lateinit var skbRating: SeekBar
    private lateinit var btnSubmit: Button
    private lateinit var tvShowSkbRating: TextView
    private lateinit var userData: User

    private var customerRating: Float? = 0.00f
    private var countRatingTime: Int? = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        customerUId = RatingMechFragmentArgs.fromBundle(requireArguments()).customerUId
        _binding = FragmentRatingMechBinding.inflate(inflater, container, false)
        val repo = UserRepository()
        ratingMechViewModelFactory = RatingMechViewModelFactory(repo)
        ratingMechViewModel = ViewModelProvider(this, ratingMechViewModelFactory)
            .get(RatingMechViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentRatingMechBinding) {
        imgProfile = binding.imgRatingProfileMech
        nameCustomer = binding.tvRatingMechCustomerName
        skbRating = binding.skbRatingMech
        btnSubmit = binding.btnRatingMechSubmit
        tvShowSkbRating = binding.tvShowSkbRatingMech
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        ratingMechViewModel.getUser(customerUId)
    }

    private fun initView() {
        DownloadImageUtils.getImageFromStorage(customerUId, this)
        skbRating.setOnSeekBarChangeListener(this)
        btnSubmit.setOnClickListener { view ->
            setUserRating()
            updateUserRating()
            view.findNavController().navigate(RatingMechFragmentDirections
                .actionRatingMechFragmentToNavigationAppointMech())
        }
    }

    private fun updateUserRating() {
        ratingMechViewModel.updateUserRating(customerUId, customerRating!!, countRatingTime!!)
    }

    private fun setUserRating() {
        if (userData.rating == null && userData.countTimeRating == null) {
            customerRating = tvShowSkbRating.text.toString().toFloat()
        } else {
            countRatingTime = countRatingTime?.plus(userData.countTimeRating!!)
            customerRating = ((userData.rating!! * userData.countTimeRating!!) + (tvShowSkbRating.text.toString().toInt())) / countRatingTime!!
        }
    }

    private fun initObserve() {
        ratingMechViewModel.getUser.observe(viewLifecycleOwner) {
            userData = it
            nameCustomer.text = it.name + " " + it.surname
        }
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgProfile.setImageBitmap(Bitmap.createScaledBitmap(
                bmp,
                imgProfile.width,
                imgProfile.height,
                false
            ))
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tvShowSkbRating.text = progress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        customerRating = null
        countRatingTime = null
    }
}
package com.example.android.finalyearproject.customer.appoint.rating

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.FragmentRatingBinding
import com.example.android.finalyearproject.databinding.FragmentRatingMechBinding
import com.example.android.finalyearproject.mechanic.appoint.rating.RatingMechFragmentArgs
import com.example.android.finalyearproject.mechanic.appoint.rating.RatingMechViewModel
import com.example.android.finalyearproject.mechanic.appoint.rating.RatingMechViewModelFactory
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository

class RatingFragment: Fragment(), DownloadImageListener, SeekBar.OnSeekBarChangeListener {

    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!
    private lateinit var ratingViewModel: RatingViewModel
    private lateinit var ratingViewModelFactory: RatingViewModelFactory

    private lateinit var mechUId: String
    private lateinit var imgProfile: ImageView
    private lateinit var mechName: TextView
    private lateinit var skbRating: SeekBar
    private lateinit var btnSubmit: Button
    private lateinit var tvShowSkbRating: TextView
    private lateinit var userData: User

    private var mechRating: Float? = 0.00f
    private var countRatingTime: Int? = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mechUId = RatingFragmentArgs.fromBundle(requireArguments()).mechUId
        _binding = FragmentRatingBinding.inflate(inflater, container, false)
        val repo = UserRepository()
        ratingViewModelFactory = RatingViewModelFactory(repo)
        ratingViewModel = ViewModelProvider(this, ratingViewModelFactory)
            .get(RatingViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentRatingBinding) {
        imgProfile = binding.imgRatingProfile
        mechName = binding.tvRatingMechName
        skbRating = binding.skbRating
        btnSubmit = binding.btnRatingSubmit
        tvShowSkbRating = binding.tvShowSkbRating
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        ratingViewModel.getUser(mechUId)
    }

    private fun initView() {
        DownloadImageUtils.getImageFromStorage(mechUId, this)
        skbRating.setOnSeekBarChangeListener(this)
        btnSubmit.setOnClickListener { view ->
            setUserRating()
            updateUserRating()
            view.findNavController().navigate(RatingFragmentDirections
                .actionRatingFragmentToNavigationAppoint())
        }
    }

    private fun setUserRating() {
        if (userData.rating == null && userData.countTimeRating == null) {
            mechRating = tvShowSkbRating.text.toString().toFloat()
        } else {
            countRatingTime = countRatingTime
                ?.plus(userData.countTimeRating!!)
            mechRating = ((userData.rating!! * userData.countTimeRating!!.toFloat())
                    + (tvShowSkbRating.text.toString().toFloat())) / countRatingTime!!
        }
    }

    private fun updateUserRating() {
        ratingViewModel.updateUserRating(mechUId, mechRating!!, countRatingTime!!)
    }

    private fun initObserve() {
        ratingViewModel.getUser.observe(viewLifecycleOwner) {
            userData = it
            mechName.text = it.name + " " + it.surname
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
        mechRating = null
        countRatingTime = null
    }
}
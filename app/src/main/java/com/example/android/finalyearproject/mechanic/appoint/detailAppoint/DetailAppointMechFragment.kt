package com.example.android.finalyearproject.mechanic.appoint.detailAppoint

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.customer.appoint.detailAppoint.LinkToGoogleMapContract
import com.example.android.finalyearproject.databinding.FragmentDetailAppointMechBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class DetailAppointMechFragment : Fragment(), DownloadImageListener {

    private var _binding: FragmentDetailAppointMechBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailAppointMechViewModel: DetailAppointMechViewModel
    private lateinit var detailAppointMechViewModelFactory: DetailAppointMechViewModelFactory

    private lateinit var imgPreview: ImageView
    private lateinit var customerName: TextView
    private lateinit var price: TextView
    private lateinit var date: TextView
    private lateinit var address: TextView
    private lateinit var type: TextView
    private lateinit var brand: TextView
    private lateinit var symptom: TextView
    private lateinit var tvAdditionalInfo: TextView
    private lateinit var edtConfirmPrice: EditText
    private lateinit var btnConfirmPrice: Button
    private lateinit var btnDismissAppoint: Button
    private lateinit var btnFinishPayout: Button
    private lateinit var tvWaitCustomerPay: TextView
    private lateinit var tvWaitCustomerRating: TextView
    private lateinit var layoutConfirmPrice: ConstraintLayout
    private lateinit var tvNotInPriceRange: TextView
    private lateinit var tvNotFillPrice: TextView
    private lateinit var tvShowConfirmPricePrefix: TextView
    private lateinit var tvShowConfirmPrice: TextView
    private lateinit var taskData: Task
    private lateinit var electricalApplianceType: String
    private lateinit var selectedOffer: Task.Offer
    private lateinit var linkToGoogleMapResult : ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        taskData = DetailAppointMechFragmentArgs.fromBundle(requireArguments()).taskData
        electricalApplianceType = taskData.type!!
        _binding = FragmentDetailAppointMechBinding.inflate(inflater, container, false)
        val taskRepo = TaskRepository()
        val userRepo = UserRepository()
        val storageRef = Firebase.storage.reference
        detailAppointMechViewModelFactory = DetailAppointMechViewModelFactory(taskRepo, userRepo, storageRef)
        detailAppointMechViewModel = ViewModelProvider(this, detailAppointMechViewModelFactory)
            .get(DetailAppointMechViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentDetailAppointMechBinding) {
        imgPreview = binding.imgItemDetailAppointMechPreview
        customerName = binding.tvItemDetailAppointMechCustomerName
        price = binding.tvItemDetailAppointMechPrice
        date = binding.tvItemDetailAppointMechDate
        address = binding.tvItemDetailAppointMechAddress
        type = binding.tvItemDetailAppointMechType
        brand = binding.tvItemDetailAppointMechBrand
        symptom = binding.tvItemDetailAppointMechSymptom
        tvAdditionalInfo = binding.tvItemDetailAppointAdditionalInfo
        edtConfirmPrice = binding.edtItemDetailAppointMechConfirmPrice
        btnConfirmPrice = binding.btnItemDetailAppointMechSubmit
        btnDismissAppoint = binding.btnItemDetailAppointMechDismissAppoint
        layoutConfirmPrice = binding.layoutItemDetailAppointMechConfirmPrice
        tvNotInPriceRange = binding.tvItemDetailAppointMechNotInPriceRange
        tvWaitCustomerPay = binding.tvItemDetailAppointMechWaitCustomerPay
        tvWaitCustomerRating = binding.tvItemDetailAppointMechWaitCustomerRating
        btnFinishPayout = binding.btnItemDetailAppointMechFinishPayout
        tvNotFillPrice = binding.tvItemDetailAppointMechNotFillPrice
        tvShowConfirmPricePrefix = binding.tvItemDetailAppointMechShowConfirmPricePrefix
        tvShowConfirmPrice = binding.tvItemDetailAppointMechShowConfirmPrice
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        detailAppointMechViewModel.setTaskData(taskData)
        detailAppointMechViewModel.updatePayoutTask(taskData.taskId!!)
    }

    private fun initView() {
        linkToGoogleMapResult = registerForActivityResult(LinkToGoogleMapContract()) {}
        taskData.taskId?.let {
            DownloadImageUtils.getImageFromStorage(it, this)
        }
        customerName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        address.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tvAdditionalInfo.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        customerName.setOnClickListener { view ->
            view.findNavController().navigate(DetailAppointMechFragmentDirections
                .actionDetailAppointMechFragmentToProfileFragment(taskData.creatorUId!!)
            )
        }
        tvAdditionalInfo.setOnClickListener { view ->
            view.findNavController().navigate(DetailAppointMechFragmentDirections
                .actionDetailAppointMechFragmentToDetailTaskFindCustomerFragment(taskData))
        }
        btnConfirmPrice.setOnClickListener {
            if (edtConfirmPrice.text.isNullOrEmpty()) {
                tvNotFillPrice.visibility = View.VISIBLE
                tvNotInPriceRange.visibility = View.GONE
            } else {
                val confirmPrice = edtConfirmPrice.text.toString().toInt()
                if (checkConfirmPriceIsInRange(confirmPrice)) {
                    detailAppointMechViewModel.updateConfirmPrice(
                        taskData.taskId!!, edtConfirmPrice.text.toString()
                    )
                } else {
                    tvNotFillPrice.visibility = View.GONE
                    tvNotInPriceRange.visibility = View.VISIBLE
                }
            }
        }
        btnDismissAppoint.setOnClickListener { view ->
            detailAppointMechViewModel.dismissTaskByTaskId(taskData.taskId!!, selectedOffer.mechUId!!, view)
        }
        btnFinishPayout.setOnClickListener { view ->
            detailAppointMechViewModel.updateFinishPayoutTask(taskData.taskId!!, selectedOffer.mechUId!!, taskData.creatorUId!!)
            view.findNavController().navigate(DetailAppointMechFragmentDirections
                .actionDetailAppointMechFragmentToRatingMechFragment(taskData.creatorUId!!))
        }
        address.setOnClickListener {
            linkToGoogleMapResult.launch(taskData.address!!)
        }
    }

    private fun checkConfirmPriceIsInRange(confirmPrice: Int): Boolean {
        val minPrice = selectedOffer.minPrice
        val maxPrice = selectedOffer.maxPrice
        return minPrice!! <= confirmPrice && confirmPrice <= maxPrice!!
    }

    private fun initObserve() {
        detailAppointMechViewModel.taskDataLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setUIData(it)
            }
        }
        detailAppointMechViewModel.updatePayoutLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.finishPayout != null) {
                    tvWaitCustomerRating.visibility = View.VISIBLE
                    layoutConfirmPrice.visibility = View.GONE
                    btnDismissAppoint.visibility = View.GONE
                    tvShowConfirmPricePrefix.visibility = View.VISIBLE
                    tvShowConfirmPrice.visibility = View.VISIBLE
                    tvShowConfirmPrice.text = it.confirmPrice + " ฿"
                }else if (it.alreadyPaid != null) {
                    btnFinishPayout.visibility = View.VISIBLE
                    tvWaitCustomerPay.visibility = View.GONE
                    layoutConfirmPrice.visibility = View.GONE
                    tvShowConfirmPricePrefix.visibility = View.VISIBLE
                    tvShowConfirmPrice.visibility = View.VISIBLE
                    tvShowConfirmPrice.text = it.confirmPrice + " ฿"
                    btnDismissAppoint.visibility = View.GONE
                } else if (it.confirmPrice != null) {
                    tvWaitCustomerPay.visibility = View.VISIBLE
                    layoutConfirmPrice.visibility = View.GONE
                    tvShowConfirmPricePrefix.visibility = View.VISIBLE
                    tvShowConfirmPrice.visibility = View.VISIBLE
                    tvShowConfirmPrice.text = it.confirmPrice + " ฿"
                    btnDismissAppoint.visibility = View.GONE
                }
            }
        }
    }

    private fun setUIData(taskData: Task) {
        selectedOffer = taskData.listOffer?.find { it.customerSelected == true }!!
        customerName.text = taskData.creatorName
        val minPrice = selectedOffer.minPrice.toString()
        val maxPrice = selectedOffer.maxPrice.toString()
        price.text = "$minPrice - $maxPrice"
        date.text = selectedOffer.confirmDate
        address.text = taskData.address
        type.text = taskData.type
        brand.text = taskData.brand
        symptom.text = taskData.symptom
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgPreview.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                imgPreview.width,
                imgPreview.height,
                false)
            )
        } else {
            when (electricalApplianceType) {
                "ตู้เย็น" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_refrigerator)
                "ทีวี" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_television)
                "พัดลม" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_fan)
                "แอร์" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_air)
                "เครื่องซักผ้า" -> imgPreview.setImageResource(R.drawable.ic_not_upload_img_washing_machine)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
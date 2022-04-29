package com.example.android.finalyearproject.customer.appoint.detailAppoint

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.SingleEvent
import com.example.android.finalyearproject.SingleEventObserver
import com.example.android.finalyearproject.databinding.FragmentDetailAppointBinding
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DetailAppointFragment: Fragment(), DownloadImageListener {

    private var _binding: FragmentDetailAppointBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailAppointViewModel: DetailAppointViewModel
    private lateinit var detailAppointViewModelFactory: DetailAppointViewModelFactory

    private lateinit var imgPreview: ImageView
    private lateinit var mechName: TextView
    private lateinit var price: TextView
    private lateinit var date: TextView
    private lateinit var address: TextView
    private lateinit var type: TextView
    private lateinit var brand: TextView
    private lateinit var symptom: TextView
    private lateinit var tvAdditionalInfo: TextView
    private lateinit var confirmPrice: TextView
    private lateinit var btnDismissAppoint: Button
    private lateinit var btnPay: Button
    private lateinit var layoutConfirmPrice: ConstraintLayout
    private lateinit var tvShowConfirmPricePrefix: TextView
    private lateinit var tvShowConfirmPrice: TextView
    private lateinit var taskData: Task
    private lateinit var electricalApplianceType: String
    private lateinit var tvWaitMechConfirmPaid: TextView
    private lateinit var linkToGoogleMapResult : ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        taskData = DetailAppointFragmentArgs.fromBundle(requireArguments()).taskData
        electricalApplianceType = taskData.type!!
        _binding = FragmentDetailAppointBinding.inflate(inflater, container, false)
        val taskRepo = TaskRepository()
        val userRepo = UserRepository()
        val storageRef = Firebase.storage.reference
        detailAppointViewModelFactory = DetailAppointViewModelFactory(taskRepo, userRepo, storageRef)
        detailAppointViewModel = ViewModelProvider(this, detailAppointViewModelFactory)
            .get(DetailAppointViewModel::class.java)
        findView(binding)

        return binding.root
    }

    private fun findView(binding: FragmentDetailAppointBinding) {
        imgPreview = binding.imgItemDetailAppointPreview
        mechName = binding.tvItemDetailAppointMechName
        price = binding.tvItemDetailAppointPrice
        date = binding.tvItemDetailAppointDate
        address = binding.tvItemDetailAppointAddress
        type = binding.tvItemDetailAppointType
        brand = binding.tvItemDetailAppointBrand
        symptom = binding.tvItemDetailAppointSymptom
        tvAdditionalInfo = binding.tvItemDetailAppointAdditionalInfo
        confirmPrice = binding.tvItemDetailAppointConfirmPrice
        btnDismissAppoint = binding.btnItemDetailAppointDismissAppoint
        btnPay = binding.btnItemDetailAppointPay
        tvWaitMechConfirmPaid = binding.tvItemDetailAppointWaitMechConfirmPaid
        layoutConfirmPrice = binding.layoutItemDetailAppointConfirmPrice
        tvShowConfirmPricePrefix = binding.tvItemDetailAppointShowConfirmPricePrefix
        tvShowConfirmPrice = binding.tvItemDetailAppointShowConfirmPrice
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve(view)
        detailAppointViewModel.setTaskData(taskData)
        detailAppointViewModel.updatePayoutTask(taskData.taskId!!)
    }

    private fun initView() {
        linkToGoogleMapResult = registerForActivityResult(LinkToGoogleMapContract()) {}
        DownloadImageUtils.getImageFromStorage(taskData.taskId!!, this)
        mechName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tvAdditionalInfo.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        address.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        mechName.setOnClickListener { view ->
            val selectedOffer = taskData.listOffer?.find { it.customerSelected == true }
            view.findNavController().navigate(DetailAppointFragmentDirections
                .actionDetailAppointFragmentToProfileMechFragment(selectedOffer?.mechUId))
        }
        tvAdditionalInfo.setOnClickListener { view ->
            view.findNavController().navigate(DetailAppointFragmentDirections
                .actionDetailAppointFragmentToDetailTaskFindCustomerFragment3(taskData))
        }
        btnPay.setOnClickListener {
            detailAppointViewModel.updateAlreadyPaid(taskData.taskId!!)
        }
        btnDismissAppoint.setOnClickListener { view ->
            detailAppointViewModel.dismissTask(taskData.taskId!!,taskData.creatorUId!!, view)
        }
        address.setOnClickListener {
            linkToGoogleMapResult.launch(taskData.address!!)
        }
    }

    private fun initObserve(view: View) {
        detailAppointViewModel.taskDataLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setUIData(it)
            }
        }
        singleEventObserve(detailAppointViewModel.updatePayoutLiveData) {
            it?.let {
                if (it.finishPayout != null) {
                    val mechUId = taskData.listOffer?.find { it.customerSelected == true }?.mechUId
                    mechUId?.let {
                        view.findNavController().navigate(DetailAppointFragmentDirections
                            .actionDetailAppointFragmentToRatingFragment(mechUId))
                    }
                    detailAppointViewModel.updateTaskStatus(taskData.taskId!!)
                } else if (it.alreadyPaid != null) {
                    tvShowConfirmPricePrefix.visibility = View.VISIBLE
                    tvShowConfirmPrice.visibility = View.VISIBLE
                    tvShowConfirmPrice.text = it.confirmPrice.toString() + " ฿"
                    tvWaitMechConfirmPaid.visibility = View.VISIBLE
                    btnDismissAppoint.visibility = View.GONE
                    layoutConfirmPrice.visibility = View.GONE
                } else if (it.confirmPrice != null) {
                    confirmPrice.text = it.confirmPrice.toString() + " ฿"
                    btnDismissAppoint.visibility = View.GONE
                    btnPay.visibility = View.VISIBLE
                }
            }
        }
//        detailAppointViewModel.updatePayoutLiveData.observe(viewLifecycleOwner) {
//            it?.let {
//                if (it.finishPayout != null) {
//                    val mechUId = taskData.listOffer?.find { it.customerSelected == true }?.mechUId
//                    mechUId?.let {
//                        view.findNavController().navigate(DetailAppointFragmentDirections
//                            .actionDetailAppointFragmentToRatingFragment(mechUId))
//                    }
////                    detailAppointViewModel.updateTaskStatus(taskData.taskId!!)
//                } else if (it.alreadyPaid != null) {
//                    tvShowConfirmPricePrefix.visibility = View.VISIBLE
//                    tvShowConfirmPrice.visibility = View.VISIBLE
//                    tvShowConfirmPrice.text = it.confirmPrice.toString() + "฿"
//                    tvWaitMechConfirmPaid.visibility = View.VISIBLE
//                    btnDismissAppoint.visibility = View.GONE
//                    layoutConfirmPrice.visibility = View.GONE
//                } else if (it.confirmPrice != null) {
//                    confirmPrice.text = it.confirmPrice.toString() + "฿"
//                    btnDismissAppoint.visibility = View.GONE
//                    btnPay.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    private fun setUIData(taskData: Task) {
        mechName.text = taskData.listOffer?.find { it.customerSelected == true }?.mechName
        val selectedOffer = taskData.listOffer?.find { it.customerSelected == true }
        val minPrice = selectedOffer?.minPrice.toString()
        val maxPrice = selectedOffer?.maxPrice.toString()
        price.text = "$minPrice - $maxPrice"
        date.text = selectedOffer?.confirmDate
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

fun <T> Fragment.singleEventObserve(
    liveData: LiveData<SingleEvent<T>>,
    body: (T) -> Unit = {}
) {
    liveData.observe(viewLifecycleOwner, SingleEventObserver { it?.let { t -> body(t) } })
}





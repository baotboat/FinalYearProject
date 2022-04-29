package com.example.android.finalyearproject.mechanic.findCustomer

import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.android.finalyearproject.DownloadImageListener
import com.example.android.finalyearproject.DownloadImageUtils
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.ItemFindCustomerTaskBinding
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository
import com.google.android.gms.maps.model.LatLng

import java.text.SimpleDateFormat
import java.util.*

class FindCustomerTaskViewHolder(
    itemView: View,
    private val context: Context,
    private val currentUserData: User,
    private val findCustomerListener: FindCustomerFragmentListener,
    private val taskRepository: TaskRepository
) : FindCustomerViewHolder(itemView), DownloadImageListener, AdapterView.OnItemSelectedListener {

    private val binding = ItemFindCustomerTaskBinding.bind(itemView)
    private val name = binding.tvItemFindCustomerCustomer
    private val type = binding.tvItemFindCustomerType
    private val brand = binding.tvItemFindCustomerBrand
    private val symptom = binding.tvItemFindCustomerSymptom
    private val imgPreview = binding.imgItemFindCustomerPreview
    private val dateSpinner = binding.dateSpinner
    private val btnSendOffer = binding.btnItemFindCustomerOffer
    private val btnDenyTask = binding.btnItemFindCustomerDenyTask
    private val edtMinPrice = binding.edtItemFindCustomerMinPrice
    private val edtMaxPrice = binding.edtItemFindCustomerMaxPrice
    private val cvFindCustomerTask = binding.cvItemFindCustomer
    private val tvWrongRangePrice = binding.tvWrongRangePrice
    private val tvNotFillPrice = binding.tvNotFillPrice
    private val tvNotFillCorrectPrice = binding.tvPleaseFillCorrectPrice
    private val tvPostedDate = binding.tvItemFindCustomerPostedDate
    private val distance = binding.tvItemFindCustomerDistance
    private val layoutToHide = binding.layoutToHide

    private lateinit var dateAdapter: ArrayAdapter<String>
    private lateinit var confirmDate: String
    private lateinit var electricalApplianceType: String
    private val geocoder = Geocoder(context, Locale.getDefault())
    private var mechDidSimilarTask = false

    companion object {
        fun create(
            parent: ViewGroup,
            currentUserData: User,
            findCustomerListener: FindCustomerFragmentListener,
            taskRepository: TaskRepository
        ): FindCustomerTaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_find_customer_task, parent, false)
            return FindCustomerTaskViewHolder(view,
                parent.context,
                currentUserData,
                findCustomerListener,
                taskRepository)
        }
    }

    fun initView(uiModel: Task) {
        setDateSpinner(uiModel.date)
        electricalApplianceType = uiModel.type!!
        checkMechDidSimilarTask(uiModel.chipSymptom!!, uiModel.type!!)
        btnSendOffer.setOnClickListener { checkConditionsBeforeSendOffer(uiModel) }
        btnDenyTask.setOnClickListener { denyTask(uiModel) }
        cvFindCustomerTask.setOnClickListener { view ->
            view.findNavController().navigate(FindCustomerFragmentDirections
                .actionFindCustomerMechToDetailTaskFindCustomerFragment(uiModel))
        }
    }

    private fun setDateSpinner(dateList: List<Date>?) {
        val arrayDate = setDateForUI(dateList).toTypedArray()
        dateAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            arrayDate
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        dateSpinner.apply {
            adapter = dateAdapter
            onItemSelectedListener = this@FindCustomerTaskViewHolder
        }
    }

    private fun checkConditionsBeforeSendOffer(task: Task) {
        if (edtMinPrice.text.toString().isNotEmpty() && edtMaxPrice.text.toString().isNotEmpty()) {
            val minPrice = edtMinPrice.text.toString().toInt()
            val maxPrice = edtMaxPrice.text.toString().toInt()
            val maxPriceRange = 0.5 * minPrice + minPrice
            when {
                maxPrice > maxPriceRange -> {
                    tvNotFillPrice.visibility = View.GONE
                    tvWrongRangePrice.visibility = View.VISIBLE
                    tvNotFillCorrectPrice.visibility = View.GONE
                }
                maxPrice < minPrice -> {
                    tvNotFillPrice.visibility = View.GONE
                    tvWrongRangePrice.visibility = View.GONE
                    tvNotFillCorrectPrice.visibility = View.VISIBLE
                }
                else -> {
                    sendOffer(task, minPrice, maxPrice)
                }
            }
        } else {
            tvNotFillPrice.visibility = View.VISIBLE
            tvWrongRangePrice.visibility = View.GONE
            tvNotFillCorrectPrice.visibility = View.GONE
        }
    }

    override fun bindUiModel(uiModel: Task) {
        val myLatLng = findLatLng(currentUserData.address!!)
        val customerLatLng = findLatLng(uiModel.address!!)
        findCustomerListener.computeDistanceFromLatLng(layoutToHide, distance, myLatLng, customerLatLng)
        DownloadImageUtils.getImageFromStorage(uiModel.taskId!!, this)
        name.text = uiModel.creatorName
        type.text = uiModel.type
        brand.text = uiModel.brand
        symptom.text = uiModel.symptom
        tvPostedDate.text = uiModel.taskId?.substring(0, 10)?.replace("-", "/")
    }

    private fun findLatLng(address: String): LatLng? {
        val addressTypeList = geocoder.getFromLocationName(address, 1)
        var latLng: LatLng? = null
        if (addressTypeList.isNotEmpty()) {
            latLng = LatLng(addressTypeList[0].latitude, addressTypeList[0].longitude)
        }
        return latLng
    }

    private fun setDateForUI(date: List<Date>?): MutableList<String> {
        val result = mutableListOf<String>()
        date?.onEach { result.add(SimpleDateFormat("dd/MM/yyyy").format(it)) }
        return result
    }

    private fun sendOffer(data: Task, minPrice: Int, maxPrice: Int) {
        val offer = Task.Offer(
            mechUId = currentUserData.uId,
            mechName = "${currentUserData.name} ${currentUserData.surname}",
            mechRating = currentUserData.rating,
            confirmDate = this.confirmDate,
            minPrice = minPrice,
            maxPrice = maxPrice,
            customerSelected = false,
            mechDidSimilarTask = mechDidSimilarTask
        )
        taskRepository.addListOfferByTaskId(data.taskId!!, offer)
        val decision = Task.Decision(
            mechUId = currentUserData.uId!!,
            decistion = "เสนอราคา"
        )
        taskRepository.addWhoSentOfferByTaskId(data.taskId!!, decision)
        layoutToHide.visibility = View.GONE
    }

    private fun checkMechDidSimilarTask(chipSymptom: String, type: String) {
        taskRepository.getTasksByStatus("งานซ่อมสำเร็จ",
            object : TaskListFirebaseCallback {
                override fun onResponse(response: Response) {
                    mechDidSimilarTask = checkSimilarTask(response, chipSymptom, type)
                }
            })
    }

    private fun checkSimilarTask(response: Response, chipSymptom: String, type: String): Boolean {
        val taskList = response.taskList?.reversed()?.filter {
            it.chipSymptom == chipSymptom && it.type == type
        }
        var didSimilarTask = false
        taskList?.onEach { task ->
            task.listOffer?.onEach {
                if (it.mechUId == currentUserData.uId && it.customerSelected == true) {
                    didSimilarTask = true
                }
            }
        }
        return didSimilarTask
    }

    private fun denyTask(data: Task) {
        val decision = Task.Decision(
            mechUId = currentUserData.uId!!,
            decistion = "ปฏิเสธ"
        )
        taskRepository.addWhoSentOfferByTaskId(data.taskId!!, decision)
        layoutToHide.visibility = View.GONE
    }

    override fun loadImageComplete(bmp: Bitmap?) {
        if (bmp != null) {
            imgPreview.setImageBitmap(Bitmap.createScaledBitmap(
                bmp,
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

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long,
    ) {
        confirmDate = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}
//        dateSpinner.adapter = ArrayAdapter(
//            context,
//            android.R.layout.simple_list_item_1
//            , android.R.id.text1
//            , arrayDate
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            dateSpinner.adapter = adapter
//        }

//val geocoder = Geocoder(context, Locale.getDefault())
//val address = geocoder.getFromLocationName(uiModel.address!!, 1)
//val locate = Location("")
//
//val lat = address[0].latitude
//val lon = address[0].longitude
//locate.latitude = lat
//locate.longitude = lon
//val locate2 = Location("")
//locate2.latitude = 13.6243904
//locate2.longitude = 100.5019168
//val distance = locate.distanceTo(locate2)
package com.example.android.finalyearproject.mechanic.findCustomer

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.android.finalyearproject.databinding.DialogFilterTaskBinding


class DialogFilterTask(
    private val findCustomerFragmentListener: FindCustomerFragmentListener
): DialogFragment() {

    private lateinit var binding: DialogFilterTaskBinding
    private lateinit var cbRefri: CheckBox
    private lateinit var cbTV: CheckBox
    private lateinit var cbFan: CheckBox
    private lateinit var cbAir: CheckBox
    private lateinit var cbWashMech: CheckBox
    private lateinit var btnSumbitFilter: Button
    private lateinit var edtFilterDistance: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogFilterTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cbRefri = binding.cbFilterTypeRefri
        cbTV = binding.cbFilterTypeTV
        cbFan = binding.cbFilterTypeFan
        cbAir = binding.cbFilterTypeAir
        cbWashMech = binding.cbFilterTypeWashMech
        btnSumbitFilter = binding.btnSumbitFilterTask
        edtFilterDistance = binding.edtDialogFilterTaskDistance
    }

    override fun onStart() {
        super.onStart()
        val width = (ViewGroup.LayoutParams.MATCH_PARENT)
        val height = (resources.displayMetrics.heightPixels * 0.5).toInt()
        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setTitle("Filter Type")
        val params = WindowManager.LayoutParams()
        params.copyFrom(dialog!!.window?.attributes)
        dialog!!.window!!.attributes = params
        btnSumbitFilter.setOnClickListener {
            val filterTypeSelected = checkFilterTypeSelected()
            val filterDistanceSelected = checkDistanceFilter()
            findCustomerFragmentListener.getSelectedFilter(filterTypeSelected, filterDistanceSelected)
            dialog?.dismiss()
        }
    }

    private fun checkFilterTypeSelected(): List<String> {
        val listSelected = mutableListOf<String>()
        if (cbRefri.isChecked) listSelected.add("ตู้เย็น")
        if (cbTV.isChecked) listSelected.add("ทีวี")
        if (cbFan.isChecked) listSelected.add("พัดลม")
        if (cbAir.isChecked) listSelected.add("แอร์")
        if (cbWashMech.isChecked) listSelected.add("เครื่องซักผ้า")

        return listSelected
    }

    private fun checkDistanceFilter() = edtFilterDistance.text.toString().toFloatOrNull()
}
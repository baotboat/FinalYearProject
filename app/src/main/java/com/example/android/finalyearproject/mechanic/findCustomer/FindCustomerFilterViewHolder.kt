package com.example.android.finalyearproject.mechanic.findCustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.databinding.ItemFindCustomerFilterBinding
import com.example.android.finalyearproject.model.Task

class FindCustomerFilterViewHolder(
    itemView: View,
    private val fragmentManager: FragmentManager,
    private val findCustomerListener: FindCustomerFragmentListener,
) : FindCustomerViewHolder(itemView) {

    private val binding = ItemFindCustomerFilterBinding.bind(itemView)
    private val filterType = binding.imgItemFindCustomerFilterFilter
    private val imgMyOffer = binding.imgItemFindCustomerFilterMyOffer

    companion object {
        fun create(
            parent: ViewGroup,
            fragmentManager: FragmentManager,
            findCustomerListener: FindCustomerFragmentListener,
        ): FindCustomerFilterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_find_customer_filter, parent, false)

            return FindCustomerFilterViewHolder(
                view,
                fragmentManager,
                findCustomerListener
            )
        }
    }

    override fun bindUiModel(uiModel: Task) {
        filterType.setOnClickListener {
            val dialog = DialogFilterTask(findCustomerListener)
            dialog.show(fragmentManager, "DialogTypeFilter")
        }
        imgMyOffer.setOnClickListener { view: View ->
            view.findNavController().navigate(FindCustomerFragmentDirections
                .actionNavigationFindCustomerMechToMyOfferFragment())
        }
    }
}
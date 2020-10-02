package com.wizzpass.hilt.ui.visitor

import com.wizzpass.hilt.ui.search.ResidentFoundFragment
import com.wizzpass.hilt.ui.search.ResidentListInfo
import com.wizzpass.hilt.ui.search.SearchResultFragment

/**
 * Created by novuyo on 30,September,2020
 */


import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.register.ResAddressViewModel
import com.wizzpass.hilt.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.*

@AndroidEntryPoint
class SearchResidentFragment : Fragment() {


    private val registerViewModel : RegisterViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels()
    private var searchView : View? = null
    var mContainerId:Int = -1
    var searchFieldUsed : String = ""
    var inputString : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchView = inflater.inflate(R.layout.fragment_visitor_search, container, false)
        mContainerId = container?.id?:-1
        return  searchView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_register.setOnClickListener {
            if (!et_carReg.text.toString().isEmpty()) {
                searchFieldUsed = "car_reg"
                inputString = et_carReg.text.toString()
                registerViewModel.fetchResidentByCarReg(et_carReg.text.toString())
                fetchResidentFromViewModel()

            } else if (!et_mobile.text.toString().isEmpty()) {
                searchFieldUsed = "mobile"
                inputString = et_mobile.text.toString()
                registerViewModel.fetchResidentByMobile(et_mobile.text.toString())
                fetchResidentFromViewModel()

            } else if (!et_address.text.toString().isEmpty()) {

                checkIfAddressExists()


            } else {
                Toast.makeText(activity, "Search Filed can not be empty", Toast.LENGTH_LONG).show()
                if (et_carReg.text.toString().isEmpty()) {
                    et_carReg.setError("Enter car registration")
                }

                if (et_mobile.text.toString().isEmpty()) {
                    et_mobile.setError("Enter mobile Number")
                }

                if (et_address.text.toString().isEmpty()) {
                    et_address.setError("Enter Address")
                }


            }


        }
    }

    fun checkIfAddressExists(){
        if(!et_address.text.toString().isEmpty()) {
            searchFieldUsed = "address"
            inputString = et_address.text.toString()
            resAddressViewModel.cheeckIfAddressExists(et_address.text.toString())
            checkAdddressDataFromViewModel()
        }

    }

    private fun checkAdddressDataFromViewModel(){
        resAddressViewModel.addressExists.observe(viewLifecycleOwner,
            Observer<ResAddress> {
                    t -> println("Received UserInfo $t")

                if(t!=null){
                    registerViewModel.fetchResidentByAddress(et_address.text.toString().substring(0,2))
                    fetchResidentsFromViewModel()

                }else {
                    et_address.setError("House number does not Exist")

                }
            }
        )
    }

    fun launchRegisterSearchResultFragment() {
        activity?.replaceFragmentWithNoHistory(SearchResultFragment(), mContainerId)
    }

    fun launchResultFragment() {
        activity?.replaceFragment(ResidentFoundFragment(), mContainerId)
    }

    fun launchResultWithDataFragment(resident : Resident) {
        activity?.replaceFragmentWithDataTest(ResidentFoundFragment(), mContainerId, resident)
    }

    fun launchResultWithListDataFragment(residents :ArrayList<Resident>) {
        activity?.replaceFragmentWithListDataTest(ResidentListInfo(), mContainerId, residents)
    }

    fun launchRegisterSearchResultFragment(inputText : String, searchString : String) {
        activity?.replaceFragmentWithStringData(SearchResultFragment(), mContainerId, inputText, searchString)
    }


    fun observeViewModel() {
        registerViewModel.fetchError().observe(viewLifecycleOwner,
            Observer<String> { t -> Toast.makeText(activity, t, Toast.LENGTH_LONG).show() })

        registerViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if (t != -1L) {
                    Toast.makeText(
                        activity,
                        "Inserted Successfully in DB $t",
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.let {
                        activity?.supportFragmentManager?.popBackStack()
                    }

                } else {
                    Toast.makeText(activity, "Insert Failed", Toast.LENGTH_LONG).show()

                }

            })
    }

    private fun fetchResidentFromViewModel(){
        registerViewModel.residentFound.observe(viewLifecycleOwner,
            Observer<Resident> {
                    t -> println("Received UserInfo2 List ${t}")
                if(t==null){
                    launchRegisterSearchResultFragment(inputString,searchFieldUsed)
                }else{

                    launchResultWithDataFragment(t)
                }


            }
        )
    }

    private fun fetchResidentsFromViewModel(){
        registerViewModel.residentsLinkedToSameAddress.observe(viewLifecycleOwner,
            Observer<MutableList<Resident>> {
                    t -> println("Test ${t}")
                if(t==null){
                    launchRegisterSearchResultFragment()
                }else if(t.size>1){
                    var list = arrayListOf<Resident>()
                    list = t as ArrayList<Resident>
                    launchResultWithListDataFragment(list)
                }else{

                    launchRegisterSearchResultFragment(inputString,searchFieldUsed)
                }

            }
        )
    }


}
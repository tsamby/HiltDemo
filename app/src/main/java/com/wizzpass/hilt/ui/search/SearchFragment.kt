package com.wizzpass.hilt.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_guard_login.*
import kotlinx.android.synthetic.main.fragment_register_resident.*
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import kotlinx.android.synthetic.main.fragment_search.*

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private val registerViewModel : RegisterViewModel by viewModels()
    private var searchView : View? = null
    var mContainerId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchView = inflater.inflate(R.layout.fragment_search, container, false)
        mContainerId = container?.id?:-1
        return  searchView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bt_register.setOnClickListener {
            if (!et_carReg.text.toString().isEmpty()) {

                registerViewModel.fetchResidentByCarReg(et_carReg.text.toString())
                fetchResidentFromViewModel()

            } else if (!et_mobile.text.toString().isEmpty()) {

                registerViewModel.fetchResidentByMobile(et_mobile.text.toString())
                fetchResidentFromViewModel()

            } else if (!et_address.text.toString().isEmpty()) {

                registerViewModel.fetchResidentByAddress(et_address.toString())
                //registerViewModel.fetchResidentData()
                fetchResidentsFromViewModel()

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



    fun launchRegisterSearchResultFragment() {
        activity?.replaceFragment(SearchResultFragment(), mContainerId)
    }

    fun launchResultFragment() {
        activity?.replaceFragment(ResidentFoundFragment(), mContainerId)
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
                    launchRegisterSearchResultFragment()
                }else{
                    launchResultFragment()
                }


            }
        )
    }

    private fun fetchResidentsFromViewModel(){
        registerViewModel.residentsLinkedToSameAddress.observe(viewLifecycleOwner,
            Observer<MutableList<Resident>> {
                    t -> println("Received UserInfo List ${t.size}")
                if(t==null){
                    launchRegisterSearchResultFragment()
                }

            }
        )
    }


    }
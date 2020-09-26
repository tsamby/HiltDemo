package com.wizzpass.hilt.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.Supervisor
import com.wizzpass.hilt.ui.register.ResAddressViewModel
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_guard_login.*
import kotlinx.android.synthetic.main.fragment_register_resident.*

/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class GuardLoginFragment : Fragment(), LifecycleOwner {

    private var guardView : View? = null
    var mContainerId:Int = -1
    private val guardLoginViewModel : GuardLoginViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels ()
    private val supervisorViewModel : SupervisorViewModel by viewModels ()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        guardView = inflater.inflate(R.layout.fragment_guard_login, container, false)
        mContainerId = container?.id?:-1
        return  guardView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.lifecycle.addObserver(guardLoginViewModel)


        fetchDataFromViewModel()

        buttonLogin.setOnClickListener {
            guardLoginViewModel.fetchGuardByPasword(getEnteredSearchDetails())
            fetchGuardFromViewModel()
        }
    }

    fun getEnteredSearchDetails() : String {
        return et_password.text.toString()
    }

    fun getResAddressDetails() : ResAddress {
        return ResAddress(
            0L,
            "362",
            "Ferndale"
        )
    }





    fun getGuardDetailsDetails() : Guard {
        return Guard(
            0L,
            "admin",
            "0000"
        )
    }

    fun getSupervisorDetailsDetails() : Supervisor {
        return Supervisor(
            0L,
            "admin",
            "0000"
        )
    }


    fun launchRegisterResidentFragment(){
        activity?.replaceFragment(ResidentRegisterFragment(), mContainerId)
    }

    fun launchSearchResidentFragment(){
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }


    private fun fetchDataFromViewModel(){
        // viewModel.fetchRoomData()
        guardLoginViewModel.guardFinalList.observe(viewLifecycleOwner,
            Observer<MutableList<Guard>> {
                    t -> println("Received UserInfo List ${t.size}")
               if(t.size==0){
                   guardLoginViewModel.insertGuardInfo(getGuardDetailsDetails())
                   supervisorViewModel.insertSupervisorInfo(getSupervisorDetailsDetails())
                   resAddressViewModel.insertResAddressInfo()
               }

            }
        )
    }

    private fun fetchGuardFromViewModel(){
        guardLoginViewModel.guardFound.observe(viewLifecycleOwner,
            Observer<Guard> {
                    t -> println("Received UserInfo List ${t}")
                if(t==null){
                    et_password.setError("Password incorrect")
                }else{
                    launchSearchResidentFragment()
                }


            }
        )
    }

    private fun fetchAddressesViewModel(){

        resAddressViewModel.residentFinalList.observe(viewLifecycleOwner,
            Observer<MutableList<ResAddress>> {
                    t -> println("Address List ${t.size}")
                if(t.size==0){
                    resAddressViewModel.insertResAddressInfo()
                }

            }
        )
    }

}
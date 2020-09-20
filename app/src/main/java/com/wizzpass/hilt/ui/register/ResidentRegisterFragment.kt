package com.wizzpass.hilt.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Resident
import kotlinx.android.synthetic.main.fragment_register_resident.*


/**
 * Created by novuyo on 20,September,2020
 */

class ResidentRegisterFragment  : Fragment(){

    private val registerViewModel : RegisterViewModel by viewModels()
    private var residentInfoView : View? = null
    var mContainerId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        residentInfoView = inflater.inflate(R.layout.fragment_register_resident, container, false)
        mContainerId = container?.id?:-1
        return  residentInfoView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_register.setOnClickListener {
            val resident = getEnteredResidentDetails()
            registerViewModel.insertResidentInfo(resident)
        }

        observeViewModel()

    }

    fun getEnteredResidentDetails() : Resident {


        return Resident(
            0L,
            et_name.text.toString(),
            et_surname.text.toString(),
            et_mobile.text.toString(),
            et_address.text.toString(),
            et_carReg.text.toString(),
            "",""
        )

    }

    fun observeViewModel(){
        registerViewModel.fetchError().observe(viewLifecycleOwner,
            Observer<String> { t -> Toast.makeText(activity,t, Toast.LENGTH_LONG).show() })

        registerViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if(t != -1L){



                    Toast.makeText(activity,"Inserted Successfully in DB $t", Toast.LENGTH_LONG).show()
                    activity?.let{

                        activity?.supportFragmentManager?.popBackStack()
                    }

                }else{
                    Toast.makeText(activity,"Insert Failed", Toast.LENGTH_LONG).show()

                }

            })
    }

}
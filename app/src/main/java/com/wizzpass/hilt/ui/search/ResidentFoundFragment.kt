package com.wizzpass.hilt.ui.search

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.wizzpass.hilt.util.setBorder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_guard_login.*
import kotlinx.android.synthetic.main.fragment_register_resident.*
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import kotlinx.android.synthetic.main.fragment_resident_found.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_search_result.bt_visitor
import java.io.File

@AndroidEntryPoint
class ResidentFoundFragment : Fragment() {


    private val registerViewModel : RegisterViewModel by viewModels()
    private var searchView : View? = null
    var mContainerId:Int = -1
    var resident: Resident? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchView = inflater.inflate(R.layout.fragment_resident_found, container, false)
        mContainerId = container?.id?:-1

        resident = arguments?.getParcelable("resident")

        return  searchView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBorder(innerConstraintLayout)
        if(resident!=null) {
            textViewName.setText(resident!!.fName)
            textViewSurname.setText(resident!!.lname)
            textViewCarReg.setText(resident!!.carReg)
            textViewMobile.setText(resident!!.mobile)
            textViewAddress.setText(resident!!.address + " " + resident!!.street_address)

            if (resident!!.profImage != null) {
                val imgFile = File(resident!!.profImage)
                if (imgFile.exists()) {
                    imageView.setColorFilter(null)
                    imageView.setImageURI(Uri.fromFile(imgFile))
                }
                if (resident!!.carImage != null) {
                    val imgFile = File(resident!!.carImage)
                    if (imgFile.exists()) {
                        imageView2.setColorFilter(null)
                        imageView2.setImageURI(Uri.fromFile(imgFile))
                    }
                }
            }
        }

        bt_enter.setOnClickListener {
            launchSearchFragment()
        }

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()

    }

    fun launchSearchFragment() {
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }

    fun launchVisitorResultFragment() {
        activity?.replaceFragment(SearchResultFragment(), mContainerId)
    }

    fun observeViewModel() {

        registerViewModel.residentFound.observe(viewLifecycleOwner,
            Observer<Resident> {
                    t -> println("Received UserInfo3 List ${t}")



            }
        )
    }




}
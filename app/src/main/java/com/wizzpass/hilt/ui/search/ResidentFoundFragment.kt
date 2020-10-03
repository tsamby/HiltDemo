package com.wizzpass.hilt.ui.search

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.setBorder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_resident_found.*
import kotlinx.android.synthetic.main.fragment_resident_found.imageView4
import kotlinx.android.synthetic.main.fragment_search_result.*
import java.io.File
import java.util.*

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

        imageView4.setOnClickListener {
            launchSearchFragment()
        }

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

                    val arr =
                        ArrayList<String>()
                    arr.add("Novuyo")
                    for (i in arr.indices) {
                    }

                    if(resident!!.carImage.size>0){
                        for (i  in resident!!.carImage.indices){
                            val imgFile = File(resident!!.carImage[i])
                            if (i==0){
                            if (imgFile.exists()) {
                                imageView2.setColorFilter(null)
                                imageView2.setImageURI(Uri.fromFile(imgFile))
                                imageView2.visibility=View.VISIBLE
                                carImageLabel.visibility = View.VISIBLE
                            }
                            }

                            if (i==1){
                                if (imgFile.exists()) {
                                    imageViewcar2.setColorFilter(null)
                                    imageViewcar2.setImageURI(Uri.fromFile(imgFile))
                                    imageViewcar2.visibility=View.VISIBLE
                                    textViewcar2.visibility = View.VISIBLE
                                }
                            }
                            if (i==2){
                                if (imgFile.exists()) {
                                    imageViewcar3.setColorFilter(null)
                                    imageViewcar3.setImageURI(Uri.fromFile(imgFile))
                                    imageViewcar3.visibility=View.VISIBLE
                                    textViewcar3.visibility = View.VISIBLE
                                }
                            }
                            if (i==3){
                                if (imgFile.exists()) {
                                    imageViewcar4.setColorFilter(null)
                                    imageViewcar4.setImageURI(Uri.fromFile(imgFile))
                                    imageViewcar4.visibility=View.VISIBLE
                                    textViewcar4.visibility = View.VISIBLE
                                }
                            }
                            if (i==4){
                                if (imgFile.exists()) {
                                    imageViewcar5.setColorFilter(null)
                                    imageViewcar5.setImageURI(Uri.fromFile(imgFile))
                                    imageViewcar5.visibility=View.VISIBLE
                                    textViewcar5.visibility = View.VISIBLE
                                }
                            }

                        }

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
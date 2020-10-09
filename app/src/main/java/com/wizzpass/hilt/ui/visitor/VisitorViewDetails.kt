package com.wizzpass.hilt.ui.visitor

/**
 * Created by novuyo on 09,October,2020
 */

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.SecondaryDriverAdapter
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.SecondaryDriver
import com.wizzpass.hilt.db.entity.Visitor
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.ui.search.SearchResultFragment
import com.wizzpass.hilt.ui.secondaryDrivers.SecondaryDriverViewModel
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.setBorder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident_two.*
import kotlinx.android.synthetic.main.fragment_resident_found.*
import kotlinx.android.synthetic.main.fragment_resident_found.bt_enter
import kotlinx.android.synthetic.main.fragment_resident_found.carImageLabel
import kotlinx.android.synthetic.main.fragment_resident_found.imageView
import kotlinx.android.synthetic.main.fragment_resident_found.imageView2
import kotlinx.android.synthetic.main.fragment_resident_found.imageView4
import kotlinx.android.synthetic.main.fragment_resident_found.innerConstraintLayout
import kotlinx.android.synthetic.main.fragment_resident_found.textViewAddress
import kotlinx.android.synthetic.main.fragment_resident_found.textViewCarReg
import kotlinx.android.synthetic.main.fragment_resident_found.textViewMobile
import kotlinx.android.synthetic.main.fragment_resident_found.textViewName
import kotlinx.android.synthetic.main.fragment_resident_found.textViewSurname
import kotlinx.android.synthetic.main.fragment_resident_found_two.*
import kotlinx.android.synthetic.main.fragment_resident_list.*
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.fragment_view_visitor_detail.*
import java.io.File
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class VisitorViewDetails : Fragment() {



    private var visitorView : View? = null
    var mContainerId:Int = -1
    var visitor: Visitor? = null
    private val mainViewModel :  VisitorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        visitorView = inflater.inflate(R.layout.fragment_view_visitor_detail, container, false)
        mContainerId = container?.id?:-1

        visitor = arguments?.getParcelable("visitor")

        return  visitorView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView4.setOnClickListener {
            launchSearchFragment()
        }

        setBorder(innerConstraintLayout)
        if(visitor!=null) {
            textViewName.setText(visitor!!.vis_fName)
            textViewSurname.setText(visitor!!.vis_lname)
            textViewCarReg.setText(visitor!!.visCarReg)
            textViewMobile.setText(visitor!!.visMobile)
            textViewAddress.setText(visitor!!.resAddress + " " + visitor!!.res_street_address)

            if (visitor!!.vis_profImage != null) {
                val imgFile = File(visitor!!.vis_profImage)
                if (imgFile.exists()) {
                    imageView.setColorFilter(null)
                    imageView.setImageURI(Uri.fromFile(imgFile))
                }
                if (visitor!!.vis_carImage != null) {

                    if(visitor!!.vis_carImage.size>0){
                        for (i  in visitor!!.vis_carImage.indices){
                            val imgFile = File(visitor!!.vis_carImage[i])
                            if (i==0){
                                if (imgFile.exists()) {
                                    imageView2.setColorFilter(null)
                                    imageView2.setImageURI(Uri.fromFile(imgFile))
                                    imageView2.visibility=View.VISIBLE
                                    carImageLabel.visibility = View.VISIBLE
                                }
                            }


                        }

                    }

                }

                if(visitor!!.visEntryTimeStamp!=null) {
                    if(!visitor!!.visEntryTimeStamp.isEmpty()) {
                        textView26.setText(visitor!!.visEntryTimeStamp.substring(0, 10))
                        textView27.setText(visitor!!.visEntryTimeStamp.substring(11, 16))
                    }
                }


            }
        }

        bt_exit.setOnClickListener {
            mainViewModel.upDateExitTime( LocalDateTime.now().toString(), visitor!!.visId)
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


    }





}

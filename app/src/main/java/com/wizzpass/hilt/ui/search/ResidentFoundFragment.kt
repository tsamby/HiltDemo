package com.wizzpass.hilt.ui.search

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.SecondaryDriverAdapter
import com.wizzpass.hilt.data.local.db.entity.Resident
import com.wizzpass.hilt.data.local.db.entity.SecondaryDriver
import com.wizzpass.hilt.ui.register.RegisterViewModel
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
import java.io.File
import java.util.*

@AndroidEntryPoint
class ResidentFoundFragment : Fragment() , SecondaryDriverAdapter.OnItemClickListener{


    private val registerViewModel : RegisterViewModel by viewModels()
    private val secondaryDriverViewModel : SecondaryDriverViewModel by viewModels()

    private var searchView : View? = null
    var mContainerId:Int = -1
    var resident: Resident? = null
    private var driversAdapter : SecondaryDriverAdapter? = null
    var searchText: String? = ""
    var dialogBuilder : android.app.AlertDialog? = null
    var drivers = arrayListOf<SecondaryDriver>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchView = inflater.inflate(R.layout.fragment_resident_found_two, container, false)
        mContainerId = container?.id?:-1

        resident = arguments?.getParcelable("resident")
        searchText = arguments?.getString("searchField")
        if(searchText!=null){
            if(searchText.equals("car_reg")){
                secondaryDriverViewModel.fetchSecondaryDriversByCarReg(resident!!.carReg)
            }else if(searchText.equals("mobile")){
                secondaryDriverViewModel.fetchResidentByMobile(resident!!.mobile)
            }else if(searchText.equals("address")){
                secondaryDriverViewModel.fetchResidentByAddress(resident!!.address)
            }
        }

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


                        }

                    }

                }


            }
        }

        bt_enter.setOnClickListener {
            launchSearchFragment()
        }

        initAdapter()

        observeViewModel()
    }

    fun uploadDriversList(secondaryDrivers : ArrayList<SecondaryDriver>){
        drivers = secondaryDrivers
        driversAdapter?.refreshAdapter(secondaryDrivers)
    }



    private fun initAdapter(){
        driversAdapter = SecondaryDriverAdapter(arrayListOf(), this)
        recylerViewResFound.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = driversAdapter

        }

    }
    override fun onItemClick(position: Int) {
        val clickedDriver : SecondaryDriver = drivers[position]
        showSecondaryDriverDetails(clickedDriver)

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
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

        secondaryDriverViewModel.secondaryDriversLinkedToSameAddress.observe(viewLifecycleOwner,
            Observer<MutableList<SecondaryDriver>> {
                    t -> println("Secondary Drivers Test ${t}")
                if(t==null){

                }else {

                    println("Secondary Drivers Test WE GOT HERE Mobile")
                    if(t.size>0) {
                        textView14.visibility = View.VISIBLE
                        recylerViewResFound.visibility = View.VISIBLE
                        uploadDriversList(t as ArrayList<SecondaryDriver>)
                    }
                }

            }
        )


        secondaryDriverViewModel.secondaryDriversLinkedToCar.observe(viewLifecycleOwner,
            Observer<MutableList<SecondaryDriver>> {
                    t -> println("Secondary Drivers Test ${t}")
                if(t==null){

                }else {

                    if(t.size>0) {
                        textView14.visibility = View.VISIBLE
                        recylerViewResFound.visibility = View.VISIBLE
                        uploadDriversList(t as ArrayList<SecondaryDriver>)
                    }

                    /*println("Secondary Drivers Test WE GOT HERE")
                    textView14.visibility = View.VISIBLE
                    recylerViewResFound.visibility = View.VISIBLE
                    uploadDriversList(t as ArrayList<SecondaryDriver>)

                     */

                }

            }
        )
    }


    fun showSecondaryDriverDetails(driver : SecondaryDriver) {

        dialogBuilder = android.app.AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.custom_driver_dialog, null)
        val textView = dialogView.findViewById<View>(R.id.textView9) as TextView
        textView.setText(driver.fName + " " + driver.lname)
        val button1 = dialogView.findViewById<View>(R.id.button) as Button
        val imageView  = dialogView.findViewById<View>(R.id.imageView8) as ImageView
        val imgFile = File(driver.profImage)

        if (imgFile.exists()) {
                imageView.setColorFilter(null)
                imageView.setImageURI(Uri.fromFile(imgFile))
            }


        button1.setOnClickListener { view ->
            dialogBuilder!!.dismiss()
            launchSearchFragment()
        }
        dialogBuilder!!.setView(dialogView)
        dialogBuilder!!.show()
        dialogBuilder!!.setOnCancelListener {


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(dialogBuilder!=null){
            dialogBuilder!!.dismiss()
        }
    }

}
package com.wizzpass.hilt.ui.search

/**
 * Created by novuyo on 26,September,2020
 */

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.ResidentAdapter
import com.wizzpass.hilt.data.local.db.entity.Resident
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.ui.visitor.VisitorDetailsFragment
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.replaceFragmentWithDataTest
import com.wizzpass.hilt.util.replaceFragmentWithResidentAndSearchField
import com.wizzpass.hilt.util.setBorder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_resident_found.*
import kotlinx.android.synthetic.main.fragment_resident_list.*
import kotlinx.android.synthetic.main.fragment_search_result.*


@AndroidEntryPoint
class ResidentListInfo : Fragment(), LifecycleOwner , ResidentAdapter.OnItemClickListener{

    private var residentInfoListView : View? = null
    var mContainerId:Int = -1
    private var residentAdapter : ResidentAdapter? = null
    var residents = arrayListOf<Resident>()
    var searchText: String? = ""
    var isVisitor : Boolean = false

    private val mainViewModel :  RegisterViewModel by viewModels()
    private val registerViewModel : RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        residentInfoListView = inflater.inflate(R.layout.fragment_resident_list, container, false)
        mContainerId = container?.id?:-1


        residents = arguments?.getParcelableArrayList<Resident>("resident") as ArrayList<Resident>
        searchText = arguments?.getString("searchField")

        if(arguments!=null) {
            isVisitor = arguments?.getBoolean("isVisitor", false)!!
            println("isVisitor ${isVisitor}")
        }

        return  residentInfoListView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.lifecycle.addObserver(mainViewModel)
        textView8.setText(residents[0].address + " " + residents[0].street_address)
        imageView5.setOnClickListener {
            launchSearchFragment()
        }

        setBorder(constraintLayout)

        initAdapter()
    }

    fun launchSearchFragment() {
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }

    override fun onResume() {
        super.onResume()
        uploadResidentList(residents)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }


    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    private fun initAdapter(){
        residentAdapter = ResidentAdapter(arrayListOf(), this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = residentAdapter

        }

    }


    fun uploadResidentList(residents : ArrayList<Resident>){
        residentAdapter?.refreshAdapter(residents)
    }

    override fun onItemClick(position: Int) {
        val clickedResident : Resident = residents[position]

        if(registerViewModel.getAdminPrefs()) {

            launchRegisterResidentResidentData(clickedResident)
        }else {
            if (isVisitor) {
                launchVisitorDetailsFragment(clickedResident)
            } else {
                launchResultWithDataFragment(clickedResident)
            }
        }

    }

    fun launchResultWithDataFragment(resident : Resident) {
        activity?.replaceFragmentWithDataTest(ResidentFoundFragment(), mContainerId, resident)
    }
    fun launchRegisterResidentResidentData(resident : Resident) {
        activity?.replaceFragmentWithDataTest(ResidentRegisterFragment(), mContainerId, resident)
    }

    fun launchVisitorDetailsFragment(resident : Resident) {
        activity?.replaceFragmentWithDataTest(VisitorDetailsFragment(), mContainerId, resident)
    }

    fun launchResultWithDataFragment(resident : Resident, searchString: String) {
        activity?.replaceFragmentWithResidentAndSearchField(ResidentFoundFragment(), mContainerId, resident,searchString)
    }


}
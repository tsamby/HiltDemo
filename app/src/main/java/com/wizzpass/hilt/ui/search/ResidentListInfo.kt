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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.ResidentAdapter
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.replaceFragmentWithDataTest
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

    private val mainViewModel :  RegisterViewModel by viewModels()

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
        launchResultWithDataFragment(clickedResident)

    }

    fun launchResultWithDataFragment(resident : Resident) {
        activity?.replaceFragmentWithDataTest(ResidentFoundFragment(), mContainerId, resident)
    }
}
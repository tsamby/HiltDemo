package com.wizzpass.hilt.ui.visitor

/**
 * Created by novuyo on 06,October,2020
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.VisitorAdapter
import com.wizzpass.hilt.data.local.db.entity.Visitor
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_resident_list.*



@AndroidEntryPoint
class CurrentVistorsFragment : Fragment(), LifecycleOwner , VisitorAdapter.OnItemClickListener{

    private var visitorInfoListView : View? = null
    var mContainerId:Int = -1
    private var visitorAdapter : VisitorAdapter? = null
    var visitors = arrayListOf<Visitor>()
    var searchText: String? = ""

    private val mainViewModel :  VisitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        visitorInfoListView = inflater.inflate(R.layout.fragment_visitors_list, container, false)
        mContainerId = container?.id?:-1

        searchText = arguments?.getString("searchField")
        return  visitorInfoListView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.lifecycle.addObserver(mainViewModel)


        mainViewModel.fetchVisitorData()

        imageView5.setOnClickListener {
            launchSearchFragment()
        }

        setBorder(constraintLayout)

        initAdapter()

        observeViewModel()
    }

    fun launchSearchFragment() {
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
        uploadResidentList(visitors)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }


    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    private fun initAdapter(){
        visitorAdapter = VisitorAdapter(arrayListOf(), this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = visitorAdapter

        }

    }


    fun uploadResidentList(residents : ArrayList<Visitor>){
        visitorAdapter?.refreshAdapter(residents)
    }

    override fun onItemClick(position: Int) {

        val clickedResident : Visitor = visitors[position]
        launchViewVisitorDetailFragment(clickedResident)

    }


    fun launchViewVisitorDetailFragment(visitor: Visitor) {
        activity?.replaceFragmentWithDataTestVisitor(VisitorViewDetails(),mContainerId, visitor)
    }

    fun observeViewModel() {


        mainViewModel.visitorFinalList.observe(viewLifecycleOwner,
            Observer<MutableList<Visitor>> {
                    t -> println("Secondary Drivers Test ${t}")
                if(t==null){

                }else {

                    if(t.size>0) {
                        recyclerView.visibility = View.VISIBLE
                        visitors = t as java.util.ArrayList<Visitor>
                        uploadResidentList(visitors)
                    }
                }

            }
        )



    }
}
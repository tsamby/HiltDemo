package com.wizzpass.hilt.ui.admin

/**
 * Created by novuyo on 17,October,2020
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wizzpass.hilt.R
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_admin_user.*
import kotlinx.android.synthetic.main.fragment_search.imageView4


@AndroidEntryPoint
class AdminUserFragment : Fragment() {



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
        searchView = inflater.inflate(R.layout.fragment_admin_user, container, false)
        mContainerId = container?.id?:-1


        return  searchView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        imageView4.setOnClickListener{

            val intent = activity?.getIntent()
            activity?.finish()
            startActivity(intent)

        }

        bt_admin.setOnClickListener {
            launchSearchFragment()

        }


        bt_report.setOnClickListener {

        }
    }


    fun launchSearchFragment() {
        activity?.replaceFragmentAsAdmin(SearchFragment(), mContainerId)
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}
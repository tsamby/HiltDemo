package com.wizzpass.hilt.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.Supervisor
import com.wizzpass.hilt.ui.login.SupervisorViewModel
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.replaceFragmentWithStringData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_guard_login.*
import kotlinx.android.synthetic.main.fragment_register_resident.*
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_result.*

@AndroidEntryPoint
class SearchResultFragment : Fragment() {


    private val supervisorViewModel : SupervisorViewModel by viewModels()
    private var searchView : View? = null
    var mContainerId:Int = -1

    var inputText: String? = ""
    var searchText: String? = ""
    var edt_pin : EditText? = null
    var dialogBuilder : android.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchView = inflater.inflate(R.layout.fragment_search_result, container, false)
        mContainerId = container?.id?:-1

        inputText = arguments?.getString("inputText")
        searchText = arguments?.getString("searchField")
        Log.d("test", inputText!!)
        Log.d("test", searchText!!)


        return  searchView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_resident.setOnClickListener {
            showSupervisorDialog()
            //launchRegisterSearchResultFragment(inputText!!,searchText!!)
        }

        bt_visitor.setOnClickListener {

        }

    }

    fun launchRegisterRegisterFragment() {
        activity?.replaceFragment(ResidentRegisterFragment(), mContainerId)
    }

    fun launchVisitorResultFragment() {
        activity?.replaceFragment(SearchResultFragment(), mContainerId)
    }

    fun launchRegisterSearchResultFragment(inputText : String, searchString : String) {
        activity?.replaceFragmentWithStringData(ResidentRegisterFragment(), mContainerId, inputText, searchString)
    }

    fun showSupervisorDialog() {

        dialogBuilder = android.app.AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.custom_supervisor_dialog, null)
        val textView = dialogView.findViewById<View>(R.id.textView9) as TextView
        val button1 = dialogView.findViewById<View>(R.id.button) as Button
        edt_pin = dialogView.findViewById<View>(R.id.et_password) as EditText

        button1.setOnClickListener { view ->

            if(edt_pin!!.text.toString().isEmpty()){
                edt_pin!!.setError("Pin can not be empty")
            }else {
                supervisorViewModel.fetchSupervisorByPasword(edt_pin!!.text.toString())
                fetchSupervisorFromViewModel()
            }
        }
        dialogBuilder!!.setView(dialogView)
        dialogBuilder!!.show()
        dialogBuilder!!.setOnCancelListener {


        }


    }

    private fun fetchSupervisorFromViewModel(){
        supervisorViewModel.supervisorFound.observe(viewLifecycleOwner,
            Observer<Supervisor> {
                    t -> println("Received UserInfo List ${t}")
                if(t==null){
                    edt_pin!!.setError("Pin incorrect")
                }else{
                    dialogBuilder!!.dismiss()
                    launchRegisterSearchResultFragment(inputText!!,searchText!!)
                }


            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        if(dialogBuilder!=null){
            dialogBuilder!!.dismiss()
        }
    }
}
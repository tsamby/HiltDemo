package com.wizzpass.hilt.ui.search

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.Visitor
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.register.ResAddressViewModel
import com.wizzpass.hilt.ui.visitor.CurrentVistorsFragment
import com.wizzpass.hilt.ui.visitor.VisitorDetailsFragment
import com.wizzpass.hilt.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import kotlinx.android.synthetic.main.fragment_search.*
import android.view.Menu as Menu1


@AndroidEntryPoint
class SearchFragment : Fragment() {


    private val registerViewModel : RegisterViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels()
    private var searchView : View? = null
    var mContainerId:Int = -1
    var searchFieldUsed : String = ""
    var inputString : String = ""
    var visInputString : String = ""
    var isVisitor : Boolean = false
    var inputText: String? = ""
    var searchText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchView = inflater.inflate(R.layout.fragment_search, container, false)
        mContainerId = container?.id?:-1

        if(arguments!=null) {
            isVisitor = arguments?.getBoolean("isVisitor", false)!!
            println("isVisitor ${isVisitor}")
            visInputString = arguments?.getString("inputText").toString()
            inputText = arguments?.getString("inputText")
            searchText = arguments?.getString("searchField")
        }

        return  searchView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(isVisitor){
            layout_carReg.visibility = View.GONE
            textView4.visibility = View.GONE
        }

        imageView4.setOnClickListener{

        val intent = activity?.getIntent()
        activity?.finish()
        startActivity(intent)

        }

        bt_register.setOnClickListener {
            if (!et_carReg.text.toString().isEmpty()) {


                searchFieldUsed = "car_reg"
                inputString = et_carReg.text.toString()
                registerViewModel.fetchResidentByCarReg(et_carReg.text.toString())
                fetchResidentFromViewModel()

            } else if (!et_mobile.text.toString().isEmpty()) {

                if(et_mobile.text.toString().length<10){
                    et_mobile.setError("Mobile number entered is incorrect")
                }else {

                    searchFieldUsed = "mobile"
                    inputString = et_mobile.text.toString()
                    registerViewModel.fetchResidentByMobile(et_mobile.text.toString())
                    fetchResidentFromViewModel()
                }

            } else if (!et_address.text.toString().isEmpty()) {

                checkIfAddressExists()


            } else {
                Toast.makeText(activity, "Search Filed can not be empty", Toast.LENGTH_LONG).show()
                if (et_carReg.text.toString().isEmpty()) {
                    et_carReg.setError("Enter car registration")
                }

                if (et_mobile.text.toString().isEmpty()) {
                    et_mobile.setError("Enter mobile Number")
                }

                if (et_address.text.toString().isEmpty()) {
                    et_address.setError("Enter Address")
                }


            }


        }


        buttonViewCurrentParkers.setOnClickListener {
            launchCuurentVisitorsFragment()
        }
    }



    fun checkIfAddressExists(){
        if(!et_address.text.toString().isEmpty()) {
            searchFieldUsed = "address"
            inputString = et_address.text.toString()
            resAddressViewModel.cheeckIfAddressExists(et_address.text.toString())
            checkAdddressDataFromViewModel()
        }

    }

    private fun checkAdddressDataFromViewModel(){
        resAddressViewModel.addressExists.observe(viewLifecycleOwner,
            Observer<ResAddress> {
                    t -> println("House Number Received UserInfo $t")

                if(t!=null){
                    registerViewModel.fetchResidentByAddress(et_address.text.toString().substring(0,2))
                    fetchResidentsFromViewModel()

                }else {
                    et_address.setError("House number does not Exist")

                }
            }
        )
    }

    fun launchRegisterSearchResultFragment() {
        activity?.replaceFragmentWithNoHistory(SearchResultFragment(), mContainerId)
    }

    fun launchResultFragment() {
        activity?.replaceFragment(ResidentFoundFragment(), mContainerId)
    }

    fun launchCuurentVisitorsFragment() {
        activity?.replaceFragment(CurrentVistorsFragment(), mContainerId)
    }

    fun launchResultWithDataFragment(resident : Resident, searchString: String) {
        activity?.replaceFragmentWithResidentAndSearchField(ResidentFoundFragment(), mContainerId, resident, searchString)
    }


    fun launchVisitorDetailWithDataFragmentFound(resident : Resident, inputText : String ,searchString: String) {
        activity?.replaceFragmentWithResidentAndSearchFieldVisitor(VisitorDetailsFragment(), mContainerId, resident, inputText,searchString)
    }

    fun launchResultWithListDataFragment(residents :ArrayList<Resident>) {
        activity?.replaceFragmentWithListDataTest(ResidentListInfo(), mContainerId, residents)
    }

    fun launchResultWithListDataFragment(residents :ArrayList<Resident>,searchString: String) {
        activity?.replaceFragmentWithListDataAndSearchField(ResidentListInfo(), mContainerId, residents, searchString)
    }

    fun launchResultWithListDataFragment(residents :ArrayList<Resident>,searchString: String, isVisitor:Boolean) {
        activity?.replaceFragmentWithListDataAndSearchFieldVisitor(ResidentListInfo(), mContainerId, residents, searchString, isVisitor)
    }




    fun launchRegisterSearchResultFragment(inputText : String, searchString : String) {
        activity?.replaceFragmentWithStringData(SearchResultFragment(), mContainerId, inputText, searchString)
    }

    fun launchVisitorDetailsFragment(inputText : String, searchString : String) {
        activity?.replaceFragmentWithStringData(VisitorDetailsFragment(), mContainerId, inputText, searchString)
    }


        fun observeViewModel() {
            registerViewModel.fetchError().observe(viewLifecycleOwner,
                Observer<String> { t -> Toast.makeText(activity, t, Toast.LENGTH_LONG).show() })

            registerViewModel.fetchInsertedId().observe(viewLifecycleOwner,
                Observer<Long> { t ->
                    if (t != -1L) {
                        Toast.makeText(
                            activity,
                            "Inserted Successfully in DB $t",
                            Toast.LENGTH_LONG
                        ).show()
                        activity?.let {
                            activity?.supportFragmentManager?.popBackStack()
                        }

                    } else {
                        Toast.makeText(activity, "Insert Failed", Toast.LENGTH_LONG).show()

                    }

                })
        }

    private fun fetchResidentFromViewModel(){
        registerViewModel.residentFound.observe(viewLifecycleOwner,
            Observer<Resident> {
                    t -> println("Received UserInfo2 List ${t}")
                if(t==null){

                    if(isVisitor){
                      //  launchVisitorDetailsFragment(inputString, "")
                        showErrorDialog()

                    }else {
                        launchRegisterSearchResultFragment(inputString, searchFieldUsed)
                    }
                }else{

                    if(isVisitor){

                        //launchVisitorDetailWithDataFragmentFound(t, inputString)
                        launchVisitorDetailWithDataFragmentFound(t, inputText!!, searchText!!)

                    }else {
                        launchResultWithDataFragment(t, searchFieldUsed)
                    }
                }


            }
        )
    }

    private fun fetchResidentsFromViewModel(){
        registerViewModel.residentsLinkedToSameAddress.observe(viewLifecycleOwner,
            Observer<MutableList<Resident>> { t ->
                println("House number Residents Test ${t}")
                if (t == null) {

                    if (isVisitor) {
                        //  launchVisitorDetailsFragment(inputString, "")
                        showErrorDialog()
                    } else {
                        launchRegisterSearchResultFragment(inputString, searchFieldUsed)
                    }

                } else if (t.size == 1) {

                    if (isVisitor) {
                        launchVisitorDetailWithDataFragmentFound(t[0], inputText!!, searchText!!)
                    } else {

                        launchResultWithDataFragment(t[0], searchFieldUsed)
                    }
                } else if (t.size > 1) {

                    var list = arrayListOf<Resident>()
                    list = t as ArrayList<Resident>

                    if (isVisitor) {

                        launchResultWithListDataFragment(list, searchFieldUsed, isVisitor)
                    } else {

                        launchResultWithListDataFragment(list, searchFieldUsed)
                    }
                } else {
                    //launchRegisterSearchResultFragment(inputString,searchFieldUsed)

                    if (isVisitor) {
                        launchVisitorDetailWithDataFragmentFound(t[0], inputText!!, searchText!!)
                    } else {
                        launchResultWithDataFragment(t[0], searchFieldUsed)
                    }

                }
            }
        )
    }

    fun showErrorDialog() {

        var dialogBuilder = android.app.AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.custom_error_dialog, null)
        val textView = dialogView.findViewById<View>(R.id.textView9) as TextView
        val button1 = dialogView.findViewById<View>(R.id.button) as Button


        button1.setOnClickListener { view ->

            dialogBuilder.dismiss()
        }
        dialogBuilder!!.setView(dialogView)
        dialogBuilder!!.show()
        dialogBuilder!!.setOnCancelListener {


        }


    }

    }
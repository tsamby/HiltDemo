package com.wizzpass.hilt.ui.visitor

import android.Manifest
import com.wizzpass.hilt.db.entity.Visitor
import com.wizzpass.hilt.ui.register.RegisterViewModel
import com.wizzpass.hilt.ui.register.ResAddressViewModel




import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.SecondaryDriverAdapter
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.SecondaryDriver
import com.wizzpass.hilt.ui.additionalVehicles.AdditionalVehiclesFragment
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.ui.secondaryDrivers.SecondaryDriverFragment
import com.wizzpass.hilt.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_capture_visitor_details.*
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_address_street
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import kotlinx.android.synthetic.main.fragment_register_resident.et_name
import kotlinx.android.synthetic.main.fragment_register_resident.et_surname
import kotlinx.android.synthetic.main.fragment_register_resident.imageView6
import kotlinx.android.synthetic.main.fragment_register_resident.img_car
import kotlinx.android.synthetic.main.fragment_register_resident.img_profile
import kotlinx.android.synthetic.main.fragment_register_resident.textView5
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by novuyo on 06,October,2020
 */

@AndroidEntryPoint
class VisitorDetailsFragment  : Fragment(){

    private val visitorDetailsViewModel : VisitorViewModel by viewModels()
    private val resAddressViewModel :ResAddressViewModel  by viewModels()



    private var visitorInfoView : View? = null
    var mContainerId:Int = -1
    var carImage : Boolean = false
    var profImage : Boolean =  false
    var additionalCars : Boolean = false
    var secondaryDrivers : Boolean = false

    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var currentPhotoPath: String
    var imgProfilePhotoPath: String? = ""
    var carProfilePhotoPath: String? = ""
    var myList: ArrayList<String> = arrayListOf()
    var addtionalCars: ArrayList<String> = arrayListOf()
    var secDrrivers: ArrayList<String> = arrayListOf()

    val REQUEST_TAKE_PHOTO = 1
    var inputText: String? = ""
    var searchText: String? = ""
    var reasonFroVisit: String? = ""

    var car_inputText: String? = ""
    var mobile_inputText: String? = ""


    private var driversAdapter : SecondaryDriverAdapter? = null
    var drivers= arrayListOf<SecondaryDriver>()
    var resident: Resident? = null
    var reasons= arrayOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        visitorInfoView = inflater.inflate(R.layout.fragment_capture_visitor_details, container, false)
        mContainerId = container?.id?:-1

        inputText = arguments?.getString("inputText")
        searchText = arguments?.getString("searchField")
        resident = arguments?.getParcelable("resident")


        return  visitorInfoView
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(resident!=null) {
            textView16.setText("RESIDENT : "+ resident?.fName + " "+resident?.lname)
            if(inputText!=null && searchText!=null)
            setUi(inputText!!, searchText!!)
        }

        imageView6.setOnClickListener {
            launchSearchFragment()
        }

        bt_register.setOnClickListener {
            //checkIfAddressExists()
            if(!reasonFroVisit.equals("Please choose reason for visit")) {
                val visitor = getEnteredVisitorDetails()
                visitorDetailsViewModel.insertVisitorInfo(visitor)
            }else{
                Toast.makeText(context, "Please choose reason for visit", Toast.LENGTH_SHORT).show()
            }
        }

        img_profile.setOnClickListener {
            profImage = true
            carImage = false
            dispatchTakePictureIntent()
        }

        img_car.setOnClickListener {
            carImage = true
            profImage = false
            dispatchTakePictureIntent()
        }

        if(resident!=null) {
            Log.d("RESIDENT", resident.toString())
            println("RESIDENT ${resident.toString()}")

        }

        reasons = resources.getStringArray(R.array.Reasons)

        if (spinner != null) {
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_item, reasons)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    /*Toast.makeText(context,
                        getString(R.string.selected_item) + " " +
                                "" + reasons[position], Toast.LENGTH_SHORT).show()*/

                    reasonFroVisit = reasons[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        observeViewModel()


    }

    fun uploadDriversList(secondaryDrivers : ArrayList<SecondaryDriver>){
        driversAdapter?.refreshAdapter(secondaryDrivers)
    }



    fun setUi(inputText : String, searchField : String){

        if(searchField.equals("car_reg")){
            et_carReg.setText(inputText)
            car_inputText = inputText

        }

        if(searchField.equals("mobile")){
            et_mobile.setText(inputText)
            mobile_inputText = inputText
        }

        if(searchField.equals("address")){
            et_address.setText(inputText)
        }
    }

    fun uploadResidentData(resident: Resident){

        if (et_name.text.toString().isEmpty()) {
            et_name.setText(resident.fName)
        }

        if (et_surname.text.toString().isEmpty()) {
            et_surname.setText(resident.lname)
        }

        if (et_address.text.toString().isEmpty()) {
            et_address.setText(resident.address)
        }

        if (et_address_street.text.toString().isEmpty()) {
            et_address_street.setText(resident.street_address)
        }


        val imgFile = File(resident.profImage)
        if (imgFile.exists()) {
            img_profile.setColorFilter(null)
            img_profile.setImageURI(Uri.fromFile(imgFile))
            imgProfilePhotoPath = resident.profImage
        }

        if(resident.carImage.size>0){
            val imgFile = File(resident.carImage[0])
            img_car.setColorFilter(null)
            img_car.setImageURI(Uri.fromFile(imgFile))
            carProfilePhotoPath = resident.carImage[0]
            myList.add(carProfilePhotoPath!!)
        }

        if(resident.mobile!=null){
            et_mobile.setText(resident.mobile)
        }

        if(resident.carReg!=null){
            et_carReg.setText(resident.carReg)
        }

    }

    fun checkIfAddressExists(){


        if(!et_address.text.toString().isEmpty()) {
            resAddressViewModel.cheeckIfAddressExists(et_address.text.toString())
            checkAdddressDataFromViewModel()
        }

    }

    fun getEnteredVisitorDetails() : Visitor {



        val bmprofile = imgProfilePhotoPath
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Visitor(
                    0L,
                    car_inputText!!,
                    mobile_inputText!!,
                    resident!!.address,
                    resident!!.street_address,
                    et_name.text.toString(),
                    et_surname.text.toString(), bmprofile!!, myList!!, reasonFroVisit!!,
                    LocalDateTime.now().toString(), "", resident!!.resId.toString()
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }

    }




    fun observeViewModel(){
        visitorDetailsViewModel.fetchError().observe(viewLifecycleOwner,
            Observer<String> {
                    t -> Toast.makeText(activity,t, Toast.LENGTH_LONG).show()
                if(t!=null){
                    if (et_name.text.toString().isEmpty()) {
                        et_name.setError("Enter name")
                    }

                    if (et_surname.text.toString().isEmpty()) {
                        et_surname.setError("Enter last name")
                    }

                    if (et_address.text.toString().isEmpty()) {
                        et_address.setError("Enter Address")
                    }

                    if (et_address_street.text.toString().isEmpty()) {
                        et_address_street.setError("Enter Address")
                    }

                    if (imgProfilePhotoPath.toString().isEmpty()) {
                        textView5.setTextColor(Color.RED)
                    }

                    if(reasonFroVisit.equals("Please choose reason for visit")){
                     Toast.makeText(context, "Please choose reason for visit", Toast.LENGTH_SHORT).show()
                    }
                }
            })


        visitorDetailsViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if(t != -1L){
                    Toast.makeText(activity,"Visitor details captured", Toast.LENGTH_LONG).show()
                    activity?.let{

                        var mobile = "+27" + resident!!.mobile.substring(1)
                        var message = ""

                        if(reasonFroVisit.equals("Delivery/Collection")){

                            message = "Hi "+resident!!.fName+","+getEnteredVisitorDetails().vis_fName+" has arrived with your delivery/collection."
                        }else if(reasonFroVisit.equals("Visiting")){
                             message = "Hi " + resident!!.fName +", "+" Your visitor " + getEnteredVisitorDetails().vis_fName + " has arrived."
                        }else{
                            message = "Hi " + resident!!.fName +", " + getEnteredVisitorDetails().vis_fName + " is here to see you."
                        }

                        try {
                            sendSmsMsgFnc(mobile, message)
                        }catch (ex : java.lang.Exception){
                            ex.printStackTrace()
                        }

                        launchSearchFragment()
                    }
                }else{
                    Toast.makeText(activity,"Visitor details not registered", Toast.LENGTH_LONG).show()

                }

            })

        visitorDetailsViewModel.checkIfDetailsMissing().observe(viewLifecycleOwner,
            Observer<String> {
                    t -> println("Test ${t}")
                if(t!=null){
                    if(t.equals("true")){

                        checkIfAddressExists()

                    }else {
                        if (et_name.text.toString().isEmpty()) {
                            et_name.setError("Enter name")
                        }

                        if (et_surname.text.toString().isEmpty()) {
                            et_surname.setError("Enter last name")
                        }

                        if (et_address.text.toString().isEmpty()) {
                            et_address.setError("Enter Address")
                        }

                        if (et_address_street.text.toString().isEmpty()) {
                            et_address_street.setError("Enter Address")
                        }

                        if (imgProfilePhotoPath.toString().isEmpty()) {
                            textView5.setTextColor(Color.RED)
                        }

                        if(reasonFroVisit.equals("Please choose reason for visit")){
                            Toast.makeText(context, "Please choose reason for visit", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{

                }
            })


    }


    fun launchRegisterSearchResultFragment(carReg : String, mobile : String, address: String,resident: Resident) {
        activity?.replaceFragmentWithStringData(SecondaryDriverFragment(), mContainerId, carReg, mobile,address,resident)
    }

    fun launchRegisterAdditionalCarFragment(carReg : String, mobile : String, address: String,resident: Resident) {
        activity?.replaceFragmentWithStringData(AdditionalVehiclesFragment(), mContainerId, carReg, mobile,address,resident)
    }





    private fun checkAdddressDataFromViewModel(){
        resAddressViewModel.addressExists.observe(viewLifecycleOwner,
            Observer<ResAddress> {
                    t -> println("Received UserInfo $t")

                if(t!=null) {

                    if (reasonFroVisit.equals("Please choose reason for visit")) {
                        Toast.makeText(
                            context, "Please select reaon for visit",
                            Toast.LENGTH_LONG
                        ).show()
                    } else{
                    val visitor = getEnteredVisitorDetails()
                    visitor.res_street_address = t.resAddressStreet
                    visitorDetailsViewModel.insertVisitorInfo(visitor)
                }
                }else {
                    et_address.setError("House number does not Exist")

                }


            }
        )
    }

    fun launchSearchFragment() {
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }

    fun launchSecondaryDriversFragment() {
        activity?.replaceFragment(SecondaryDriverFragment(), mContainerId)
    }

    fun launchAdditionalCarsFragment() {
        activity?.replaceFragmentWithNoHistory(AdditionalVehiclesFragment(), mContainerId)
    }


    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.wizzpass.hilt.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imgFile = File(currentPhotoPath)
            if (imgFile.exists()) {
                if(profImage) {
                    img_profile.setColorFilter(null)
                    img_profile.setImageURI(Uri.fromFile(imgFile))
                    imgProfilePhotoPath = currentPhotoPath

                }else if(carImage){
                    img_car.setColorFilter(null)
                    img_car.setImageURI(Uri.fromFile(imgFile))
                    carProfilePhotoPath = currentPhotoPath
                    myList.add(carProfilePhotoPath!!)


                }


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()



    }

    fun sendSmsMsgFnc(mblNumVar: String?, smsMsgVar: String?) {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.SEND_SMS
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val smsMgrVar: SmsManager = SmsManager.getDefault()
                smsMgrVar.sendTextMessage(mblNumVar, null, smsMsgVar, null, null)
                Toast.makeText(
                    context, "Message Sent",
                    Toast.LENGTH_LONG
                ).show()
            } catch (ErrVar: Exception) {
                Toast.makeText(
                    context, ErrVar.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
                ErrVar.printStackTrace()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 10)
            }
        }
    }


}
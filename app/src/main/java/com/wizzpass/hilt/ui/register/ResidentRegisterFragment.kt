package com.wizzpass.hilt.ui.register

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.R
import com.wizzpass.hilt.adapter.SecondaryDriverAdapter
import com.wizzpass.hilt.data.local.db.entity.ResAddress
import com.wizzpass.hilt.data.local.db.entity.Resident
import com.wizzpass.hilt.data.local.db.entity.SecondaryDriver
import com.wizzpass.hilt.ui.additionalVehicles.AdditionalVehiclesFragment
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.ui.secondaryDrivers.SecondaryDriverFragment
import com.wizzpass.hilt.ui.secondaryDrivers.SecondaryDriverViewModel
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.replaceFragmentWithNoHistory
import com.wizzpass.hilt.util.replaceFragmentWithStringData
import dagger.hilt.android.AndroidEntryPoint
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
import kotlinx.android.synthetic.main.fragment_register_resident_two.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class ResidentRegisterFragment  : Fragment(), SecondaryDriverAdapter.OnItemClickListener{

    private val registerViewModel : RegisterViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels()
    private val secondaryDriverViewModel : SecondaryDriverViewModel by viewModels()


    private var residentInfoView : View? = null
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
    var isUpdate : Boolean = false

    private var driversAdapter : SecondaryDriverAdapter? = null
    var drivers= arrayListOf<SecondaryDriver>()
    var resident: Resident? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        residentInfoView = inflater.inflate(R.layout.fragment_register_resident_two, container, false)
        mContainerId = container?.id?:-1

        inputText = arguments?.getString("inputText")
        searchText = arguments?.getString("searchField")
        resident = arguments?.getParcelable("resident")



        return  residentInfoView
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(resident==null) {
            setUi(inputText!!, searchText!!)
        }

        imageView6.setOnClickListener {
            launchSearchFragment()
        }

        buttonAdditionalCar.setOnClickListener {
            additionalCars=true
            secondaryDrivers = false
            registerViewModel.checkResidentInfo(getEnteredResidentDetails())

        }
        buttonAdditionalDrivers.setOnClickListener {
            secondaryDrivers = true
            additionalCars=false
            registerViewModel.checkResidentInfo(getEnteredResidentDetails())

        }

        bt_register.setOnClickListener {
            checkIfAddressExists()
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

        button2.setOnClickListener {
            profImage = true
            carImage = false
                pickImageFromGallery();
            }

        button3.setOnClickListener {
            carImage = true
            profImage = false
            pickImageFromGallery();
        }



        if(resident!=null) {
            uploadResidentData(resident!!)
            secondaryDriverViewModel.fetchSecondaryDriversByCarReg(resident!!.carReg)
            if(registerViewModel.getAdminPrefs()){
                println("isTestrEG ${registerViewModel.getAdminPrefs()}")
                bt_register.visibility = View.GONE
                bt_update.visibility = View.VISIBLE
                bt_delete.visibility = View.VISIBLE
            }

        }

        bt_delete.setOnClickListener {
            if(resident!=null) {
               registerViewModel.deleteResidentData(resident!!)
                launchSearchFragment()
            }
        }

        bt_update.setOnClickListener {
            isUpdate= true
            checkIfAddressExists()
        }


        observeViewModel()

        initAdapter()
    }

    fun uploadDriversList(secondaryDrivers : ArrayList<SecondaryDriver>){
        driversAdapter?.refreshAdapter(secondaryDrivers)
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }


    private fun initAdapter(){
        driversAdapter = SecondaryDriverAdapter(arrayListOf(), this)
        recyclerView2.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = driversAdapter

        }

    }

    override fun onItemClick(position: Int) {


    }

    fun setUi(inputText : String, searchField : String){

        if(searchField.equals("car_reg")){
            et_carReg.setText(inputText)
        }

        if(searchField.equals("mobile")){
            et_mobile.setText(inputText)
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

    fun getEnteredResidentDetails() : Resident {

        val bmprofile = imgProfilePhotoPath

        return Resident(
            0L,
            et_carReg.text.toString(),
            et_mobile.text.toString(),
            et_address.text.toString(),
            et_address_street.text.toString(),
            et_name.text.toString(),
            et_surname.text.toString(), bmprofile!!,myList!!,addtionalCars,secDrrivers
        )

    }

      fun updateResidentDetails(resId : Long) : Resident {

        val bmprofile = imgProfilePhotoPath

        return Resident(
           resId,
            et_carReg.text.toString(),
            et_mobile.text.toString(),
            et_address.text.toString(),
            et_address_street.text.toString(),
            et_name.text.toString(),
            et_surname.text.toString(), bmprofile!!,myList!!,addtionalCars,secDrrivers
        )

    }






    fun observeViewModel(){
        registerViewModel.fetchError().observe(viewLifecycleOwner,
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
                }
            })

        registerViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if(t != -1L){
                        Toast.makeText(
                            activity,
                            "Resident successfully registered",
                            Toast.LENGTH_LONG
                        ).show()

                    activity?.let{
                        launchSearchFragment()
                    }
                }else{
                    Toast.makeText(activity,"Resident not registered", Toast.LENGTH_LONG).show()

                }

            })


        registerViewModel.checkIfDetailsMissing().observe(viewLifecycleOwner,
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
                    }
                }else{
                    //launchRegisterSearchResultFragment(et_carReg.text.toString(),et_mobile.text.toString(), et_address.text.toString())
                    //launchSearchFragment()
                }
            })

        secondaryDriverViewModel.secondaryDriverFinalList.observe(viewLifecycleOwner,
            Observer<MutableList<SecondaryDriver>> {
                    t -> println("All sec drivers ${t}")
                if(t==null){

                }else {

                }

            }
        )

        secondaryDriverViewModel.secondaryDriversLinkedToCar.observe(viewLifecycleOwner,
            Observer<MutableList<SecondaryDriver>> {
                    t -> println("Secondary Drivers Test ${t}")
                if(t==null){

                }else {

                    println("Secondary Drivers Test WE GOT HERE")
                    textView15.visibility = View.VISIBLE
                    recyclerView2.visibility = View.VISIBLE
                    uploadDriversList(t as ArrayList<SecondaryDriver>)
                }

            }
        )



    }


    fun launchRegisterSearchResultFragment(carReg : String, mobile : String, address: String,resident: Resident) {
        activity?.replaceFragmentWithStringData(SecondaryDriverFragment(), mContainerId, carReg, mobile,address,resident)
    }

    fun launchRegisterAdditionalCarFragment(carReg : String, mobile : String, address: String,resident: Resident) {
        activity?.replaceFragmentWithStringData(AdditionalVehiclesFragment(), mContainerId, carReg, mobile,address,resident)
    }


private fun findSecondaryDriver(){
    secondaryDriverViewModel.secondaryDriverFound.observe(viewLifecycleOwner,
        Observer<SecondaryDriver> {
                t -> println("Secondary Driver Test ${t}")
            if(t==null){

            }else {

            }

        }
    )
}

    private fun checkAdddressDataFromViewModel(){
        resAddressViewModel.addressExists.observe(viewLifecycleOwner,
            Observer<ResAddress> {
                    t -> println("Received UserInfo $t")

               if(t!=null){

                   if(secondaryDrivers){
                       launchRegisterSearchResultFragment(et_carReg.text.toString(),et_mobile.text.toString(), et_address.text.toString(),getEnteredResidentDetails())
                   }else if(additionalCars){
                       launchRegisterAdditionalCarFragment(et_carReg.text.toString(),et_mobile.text.toString(), et_address.text.toString(),getEnteredResidentDetails())
                   }else {

                       if(isUpdate){
                           val resident = updateResidentDetails(resident!!.resId)
                           resident.street_address = t.resAddressStreet
                           registerViewModel.updateResidentData(resident)
                           Toast.makeText(activity,"Resident information updated", Toast.LENGTH_LONG).show()
                           launchSearchFragment()
                       }else {
                           val resident = getEnteredResidentDetails()
                           resident.street_address = t.resAddressStreet
                           registerViewModel.insertResidentInfo(resident)
                       }
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

         if ( requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK ){

             val imgFile = File(getRealPathFromURI(data?.data!!))
             if (imgFile.exists()) {
                 if(profImage) {
                     img_profile.setColorFilter(null)
                     img_profile.setImageURI(data?.data)
                     imgProfilePhotoPath = getRealPathFromURI(data?.data!!)

                 }else if(carImage){
                     img_car.setColorFilter(null)
                     img_car.setImageURI(data?.data)
                     myList.clear()
                     carProfilePhotoPath = getRealPathFromURI(data?.data!!)
                     myList.add(carProfilePhotoPath!!)


                 }


             }

         }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? =
            activity?.getContentResolver()?.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }

        Log.d("Result", result!!)
        return result
    }

    override fun onDestroy() {
        super.onDestroy()

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
package com.wizzpass.hilt.ui.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.ui.search.ResidentFoundFragment
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.getStringImage
import com.wizzpass.hilt.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.*
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class ResidentRegisterFragment  : Fragment(){

    private val registerViewModel : RegisterViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels()


    private var residentInfoView : View? = null
    var mContainerId:Int = -1
    var carImage : Boolean = false
    var profImage : Boolean =  false
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    var imgProfilePhotoPath: String? = ""
    var carProfilePhotoPath: String? = ""
    val REQUEST_TAKE_PHOTO = 1
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
        residentInfoView = inflater.inflate(R.layout.fragment_register_resident, container, false)
        mContainerId = container?.id?:-1

        inputText = arguments?.getString("inputText")
        searchText = arguments?.getString("searchField")

        Log.d("test", inputText!!)
        Log.d("test", searchText!!)

        return  residentInfoView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUi(inputText!!,searchText!!)

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



        observeViewModel()

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

    fun checkIfAddressExists(){
        if(!et_address.text.toString().isEmpty()) {
            resAddressViewModel.cheeckIfAddressExists(et_address.text.toString())
            checkAdddressDataFromViewModel()
        }

    }

    fun getEnteredResidentDetails() : Resident {

        val bmprofile = imgProfilePhotoPath

        val bmCar = carProfilePhotoPath


        return Resident(
            0L,
            et_carReg.text.toString(),
            et_mobile.text.toString(),
            et_address.text.toString(),
            et_address_street.text.toString(),
            et_name.text.toString(),
            et_surname.text.toString(), bmprofile!!,bmCar!!
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
                    Toast.makeText(activity,"Resident successfully registered", Toast.LENGTH_LONG).show()
                    activity?.let{
                        launchSearchFragment()
                    }
                }else{
                    Toast.makeText(activity,"Resident not registered", Toast.LENGTH_LONG).show()

                }

            })
    }


    private fun checkAdddressDataFromViewModel(){
        resAddressViewModel.addressExists.observe(viewLifecycleOwner,
            Observer<ResAddress> {
                    t -> println("Received UserInfo $t")

                if(t!=null){
                    val resident = getEnteredResidentDetails()
                    resident.street_address = t.resAddressStreet
                    registerViewModel.insertResidentInfo(resident)

                }else {
                    et_address.setError("House number does not Exist")

                }
            }
        )
    }

    fun launchSearchFragment() {
        activity?.replaceFragment(SearchFragment(), mContainerId)
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

                }else{
                    img_car.setColorFilter(null)
                    img_car.setImageURI(Uri.fromFile(imgFile))
                    carProfilePhotoPath = currentPhotoPath

                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }





}
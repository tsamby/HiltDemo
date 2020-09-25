package com.wizzpass.hilt.ui.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mindorks.paracamera.Camera
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.util.getStringImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.*
import java.io.File
import java.io.IOException
import java.security.cert.Extension
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class ResidentRegisterFragment  : Fragment(){

    private val registerViewModel : RegisterViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels ()

    private var residentInfoView : View? = null
    var mContainerId:Int = -1
    var carImage : Boolean = false
    var profImage : Boolean =  false
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    val REQUEST_TAKE_PHOTO = 1

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
        return  residentInfoView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resAddress = getResAddressDetails()
        resAddressViewModel.insertResidentInfo(resAddress)

        bt_register.setOnClickListener {
            val resident = getEnteredResidentDetails()
            registerViewModel.insertResidentInfo(resident)
        }



        img_profile.setOnClickListener {



            profImage = true

        }

        img_car.setOnClickListener {

            carImage = true

        }



        observeViewModel()

    }

    fun getEnteredResidentDetails() : Resident {

        val bmprofile = (img_profile.getDrawable() as BitmapDrawable).bitmap

        val bmCar = (img_car.getDrawable() as BitmapDrawable).bitmap

        return Resident(
            0L,
            et_carReg.text.toString(),
            et_mobile.text.toString(),
            et_address.text.toString(),
            et_name.text.toString(),
            et_surname.text.toString(),
            getStringImage(bmprofile),getStringImage(bmCar)
        )

    }

    fun getResAddressDetails() : ResAddress {
        return ResAddress(
            0L,
            "362 Ferndale Randburg"
        )
    }




    fun observeViewModel(){
        registerViewModel.fetchError().observe(viewLifecycleOwner,
            Observer<String> { t -> Toast.makeText(activity,t, Toast.LENGTH_LONG).show() })

        registerViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if(t != -1L){



                    Toast.makeText(activity,"Inserted Successfully in DB $t", Toast.LENGTH_LONG).show()
                    activity?.let{

                        activity?.supportFragmentManager?.popBackStack()
                    }

                }else{
                    Toast.makeText(activity,"Insert Failed", Toast.LENGTH_LONG).show()

                }

            })
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
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
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            img_profile.setImageBitmap(imageBitmap)
        }
    }


    override fun onDestroy() {
        super.onDestroy()

    }



}
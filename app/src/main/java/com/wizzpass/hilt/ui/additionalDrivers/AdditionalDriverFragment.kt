package com.wizzpass.hilt.ui.additionalDrivers



/**
 * Created by novuyo on 03,October,2020
 */


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.*
import kotlinx.android.synthetic.main.fragment_register_resident.bt_register
import kotlinx.android.synthetic.main.fragment_register_resident.et_address
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_mobile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class AdditionalDriverFragment  : Fragment(){




    private var residentInfoView : View? = null
    var mContainerId:Int = -1
    var carImage : Boolean = false
    var profImage : Boolean =  false
    var carImage2 : Boolean = false
    var carImage3 : Boolean = false
    var carImage4 : Boolean = false
    var carImage5 : Boolean = false
    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var currentPhotoPath: String
    var imgProfilePhotoPath: String? = ""
    var carProfilePhotoPath: String? = ""
    var carProfilePhotoPath2: String? = ""
    var carProfilePhotoPath3: String? = ""
    var carProfilePhotoPath4: String? = ""
    var carProfilePhotoPath5: String? = ""
    var myList: ArrayList<String> = arrayListOf()

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
        residentInfoView = inflater.inflate(R.layout.fragment_additional_cars, container, false)
        mContainerId = container?.id?:-1


        return  residentInfoView
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        imageView6.setOnClickListener {
            launchSearchFragment()
        }





        img_car.setOnClickListener {

            carImage = true
            profImage = false
            carImage2 = false
            carImage3 = false
            carImage4 = false
            carImage5 = false
            dispatchTakePictureIntent()

        }


        /*floatingActionButton3.setOnClickListener(clickListener)
        floatingActionButton3.visibility=View.GONE

        floatingActionButton4.setOnClickListener(clickListener)
        img_car2.setOnClickListener(clickListener)
        textViewcar2.visibility=View.GONE
        img_car2.visibility=View.GONE
        floatingActionButton4.visibility=View.GONE


        floatingActionButton5.setOnClickListener(clickListener)
        img_car3.setOnClickListener(clickListener)
        textViewcar3.visibility=View.GONE
        img_car3.visibility=View.GONE
        floatingActionButton5.visibility=View.GONE

        floatingActionButton6.setOnClickListener(clickListener)
        img_car4.setOnClickListener(clickListener)
        textViewcar4.visibility=View.GONE
        img_car4.visibility=View.GONE
        floatingActionButton6.visibility=View.GONE



        textViewcar5.visibility=View.GONE
        img_car5.visibility=View.GONE*/






    }



    /*@SuppressLint("RestrictedApi")
    val clickListener = View.OnClickListener { view ->

        when (view.getId()) {
            R.id.floatingActionButton3 -> {
                carImage2 = true
                profImage = false
                carImage = false
                carImage3 = false
                carImage4 = false
                carImage5 = false
                dispatchTakePictureIntent()

            }
            R.id.floatingActionButton4 ->{
                carImage3 = true
                profImage = false
                carImage2 = false
                carImage = false
                carImage4 = false
                carImage5 = false
                dispatchTakePictureIntent()

            }
            R.id.floatingActionButton5 ->{
                carImage4= true
                profImage = false
                carImage2 = false
                carImage3 = false
                carImage = false
                carImage5 = false
                dispatchTakePictureIntent()

            }
            R.id.floatingActionButton6 ->{
                carImage5= true
                profImage = false
                carImage2 = false
                carImage3 = false
                carImage4 = false
                carImage = false
                dispatchTakePictureIntent()
            }

        }
    }*/

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
                    floatingActionButton3.visibility=View.VISIBLE

                }

                /*else if(carImage2){
                    img_car2.setColorFilter(null)
                    img_car2.setImageURI(Uri.fromFile(imgFile))
                    carProfilePhotoPath2 = currentPhotoPath
                    myList.add(carProfilePhotoPath2!!)
                    floatingActionButton4.visibility =View.VISIBLE
                    floatingActionButton3.visibility=View.GONE
                    textViewcar2.visibility=View.VISIBLE
                    img_car2.visibility=View.VISIBLE

                } else if(carImage3){
                    img_car3.setColorFilter(null)
                    img_car3.setImageURI(Uri.fromFile(imgFile))
                    carProfilePhotoPath3 = currentPhotoPath
                    myList.add(carProfilePhotoPath3!!)
                    floatingActionButton5.visibility=View.VISIBLE
                    floatingActionButton4.visibility =View.GONE
                    textViewcar3.visibility=View.VISIBLE
                    img_car3.visibility=View.VISIBLE

                } else if(carImage4){
                    img_car4.setColorFilter(null)
                    img_car4.setImageURI(Uri.fromFile(imgFile))
                    carProfilePhotoPath4 = currentPhotoPath
                    myList.add(carProfilePhotoPath4!!)
                    floatingActionButton6.visibility=View.VISIBLE
                    floatingActionButton5.visibility=View.GONE
                    textViewcar4.visibility=View.VISIBLE
                    img_car4.visibility=View.VISIBLE

                }else if(carImage5){
                    img_car5.setColorFilter(null)
                    img_car5.setImageURI(Uri.fromFile(imgFile))
                    carProfilePhotoPath5 = currentPhotoPath
                    myList.add(carProfilePhotoPath5!!)
                    textViewcar5.visibility=View.VISIBLE
                    img_car5.visibility=View.VISIBLE
                }

                 */

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }











}
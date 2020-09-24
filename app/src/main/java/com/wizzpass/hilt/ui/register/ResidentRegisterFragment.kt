package com.wizzpass.hilt.ui.register

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mindorks.paracamera.Camera
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.util.getStringImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.*
import java.security.cert.Extension


/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class ResidentRegisterFragment  : Fragment(){

    private val registerViewModel : RegisterViewModel by viewModels()
    private var residentInfoView : View? = null
    var mContainerId:Int = -1
    lateinit var camera:Camera
    var carImage : Boolean = false
    var profImage : Boolean =  false
    var bitmap : Bitmap? = null
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
        bt_register.setOnClickListener {
            val resident = getEnteredResidentDetails()
            registerViewModel.insertResidentInfo(resident)
        }

        camera = Camera.Builder()
            .resetToCorrectOrientation(true) // it will rotate the camera bitmap to the correct orientation from meta data
            .setTakePhotoRequestCode(1)
            .setDirectory("pics")
            .setName("ali_" + System.currentTimeMillis())
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(75)
            .setImageHeight(1000) // it will try to achieve this height as close as possible maintaining the aspect ratio;
            .build(this)

        img_profile.setOnClickListener {



            profImage = true
            camera.takePicture()
        }

        img_car.setOnClickListener {

            carImage = true
            camera.takePicture()
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


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            bitmap = camera.cameraBitmap


            if (bitmap != null) {
                if(profImage) {
                    img_profile.setImageBitmap(bitmap)

                }else{
                    img_car.setImageBitmap(bitmap)
                }
            } else {
                Toast.makeText(
                    this.requireActivity().getApplicationContext(),
                    "Picture not taken!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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


    override fun onDestroy() {
        super.onDestroy()
        camera.deleteImage()
    }
}
package com.wizzpass.hilt.ui.additionalVehicles



/**
 * Created by novuyo on 03,October,2020
 */


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Vehicles
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_resident.et_carReg
import kotlinx.android.synthetic.main.fragment_register_resident.et_name
import kotlinx.android.synthetic.main.fragment_register_resident.et_surname
import kotlinx.android.synthetic.main.fragment_register_resident.imageView6
import kotlinx.android.synthetic.main.fragment_register_resident.img_car
import kotlinx.android.synthetic.main.fragment_register_resident.img_profile
import kotlinx.android.synthetic.main.fragment_secondary_drivers.bt_add
import kotlinx.android.synthetic.main.fragment_secondary_drivers.bt_done
import kotlinx.android.synthetic.main.fragment_secondary_drivers.bt_save
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.adapter.AdditionalVehicleAdapter
import com.wizzpass.hilt.adapter.ResidentAdapter
import com.wizzpass.hilt.db.entity.Resident
import kotlinx.android.synthetic.main.fragment_additional_cars.*
import kotlinx.android.synthetic.main.fragment_resident_list.*

@AndroidEntryPoint
class AdditionalVehiclesFragment  : Fragment(), AdditionalVehicleAdapter.OnItemClickListener{

    private var residentInfoView : View? = null
    var mContainerId:Int = -1
    var carImage : Boolean = false

    private var vehiclesAdapter : AdditionalVehicleAdapter? = null
    var vehicles= arrayListOf<Vehicles>()

    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var currentPhotoPath: String
    var carProfilePhotoPath: String? = ""
    var myList: ArrayList<String> = arrayListOf()
    val REQUEST_TAKE_PHOTO = 1
    var mobile_reg: String? = ""
    var add_reg: String? = ""
    private val vehiclesViewModel : AdditionalVehiclesViewModel by viewModels()

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

        mobile_reg = arguments?.getString("mobile")
        add_reg = arguments?.getString("address")

        return  residentInfoView
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView6.setOnClickListener {
        }

        img_car.setOnClickListener {
            dispatchTakePictureIntent()
        }

        bt_save.setOnClickListener {
            val vehicle = getEnteredVehicleDetails()
            vehiclesViewModel.insertVehicleInfo(vehicle)
        }


        bt_add.setOnClickListener {
            bt_add.visibility=View.GONE
            bt_done.visibility = View.GONE
            bt_save.visibility = View.VISIBLE
            et_carReg.setText("")
            img_car.setColorFilter(null)
            img_car.setImageDrawable(null);
            img_car.setBackgroundResource(R.drawable.cam);
            carProfilePhotoPath= ""
        }
        bt_done.setOnClickListener {

        }
        observeViewModel()

        initAdapter()
    }
    fun observeViewModel(){
        vehiclesViewModel.fetchError().observe(viewLifecycleOwner,
            Observer<String> {
                    t -> Toast.makeText(activity,t, Toast.LENGTH_LONG).show()
                if(t!=null){

                }
            })

        vehiclesViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if(t != -1L){
                    Toast.makeText(activity,"Secondary vehicle saved ", Toast.LENGTH_LONG).show()
                    activity?.let{

                        bt_add.visibility=View.VISIBLE
                        bt_done.visibility = View.VISIBLE
                        bt_save.visibility = View.GONE
                        vehicles.add(getEnteredVehicleDetails())
                        uploadVehicleList(vehicles)


                    }
                }else{
                    Toast.makeText(activity,"Secondary Vehicle not saved", Toast.LENGTH_LONG).show()

                }

            })
    }

    fun uploadVehicleList(vehicles : ArrayList<Vehicles>){
        vehiclesAdapter?.refreshAdapter(vehicles)
    }



    private fun initAdapter(){
        vehiclesAdapter = AdditionalVehicleAdapter(arrayListOf(), this)
        recyclerview_add_cars.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = vehiclesAdapter

        }

    }

    override fun onItemClick(position: Int) {


    }


    fun getEnteredVehicleDetails() : Vehicles {
        val bmprofile = carProfilePhotoPath
        return Vehicles(
            et_carReg.text.toString(),
            mobile_reg!!,
            add_reg!!,
            bmprofile!!
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
                img_car.setColorFilter(null)
                img_car.setImageURI(Uri.fromFile(imgFile))
                carProfilePhotoPath = currentPhotoPath
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }











}
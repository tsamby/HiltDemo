package com.wizzpass.hilt.ui.secondaryDrivers



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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.SecondaryDriver
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_secondary_drivers.*
import kotlinx.android.synthetic.main.fragment_secondary_drivers.et_name
import kotlinx.android.synthetic.main.fragment_secondary_drivers.et_surname
import kotlinx.android.synthetic.main.fragment_secondary_drivers.imageView6
import kotlinx.android.synthetic.main.fragment_secondary_drivers.img_profile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizzpass.hilt.adapter.AdditionalVehicleAdapter
import com.wizzpass.hilt.adapter.SecondaryDriverAdapter
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.Vehicles
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.util.replaceFragmentWithDataTest
import kotlinx.android.synthetic.main.fragment_additional_cars.*
import kotlinx.android.synthetic.main.fragment_additional_cars.img_car
import kotlinx.android.synthetic.main.fragment_register_resident.*
import kotlinx.android.synthetic.main.fragment_secondary_drivers.bt_add
import kotlinx.android.synthetic.main.fragment_secondary_drivers.bt_done
import kotlinx.android.synthetic.main.fragment_secondary_drivers.bt_save


/**
 * Created by novuyo on 03,October,2020
 */

@AndroidEntryPoint
class SecondaryDriverFragment  : Fragment(),SecondaryDriverAdapter.OnItemClickListener{



    private var secDriverInfoView : View? = null
    var mContainerId:Int = -1
    var profImage : Boolean =  false
    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var currentPhotoPath: String
    var imgProfilePhotoPath: String? = ""
    val REQUEST_TAKE_PHOTO = 1

    private val secondaryDriverViewModel : SecondaryDriverViewModel by viewModels()
    var carRegistration: String? = ""
    var mobile_reg: String? = ""
    var add_reg: String? = ""

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
        secDriverInfoView = inflater.inflate(R.layout.fragment_secondary_drivers, container, false)
        mContainerId = container?.id?:-1

        carRegistration = arguments?.getString("carReg")
        mobile_reg = arguments?.getString("mobile")
        add_reg = arguments?.getString("address")
        resident = arguments?.getParcelable("resident")




        return  secDriverInfoView
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageView6.setOnClickListener {

        }

        bt_save.setOnClickListener {
            val secondaryDriver = getEnteredSecondaryDriverDetails()
            secondaryDriverViewModel.insertSecondaryDriverInfo(secondaryDriver)
        }


        bt_add.setOnClickListener {
            bt_add.visibility=View.GONE
            bt_done.visibility = View.GONE
            bt_save.visibility = View.VISIBLE
            bt_cancel.visibility = View.VISIBLE
            et_name.setText("")
            et_surname.setText("")
            img_profile.setColorFilter(null)
            img_profile.setImageDrawable(null);
            img_profile.setBackgroundResource(R.drawable.cam);
            imgProfilePhotoPath= ""
        }

        bt_done.setOnClickListener {

            launchRegisterFragment(resident!!)

        }


        img_profile.setOnClickListener {
            profImage = true
            dispatchTakePictureIntent()

        }

        button4.setOnClickListener {
            profImage = true
            pickImageFromGallery();
        }



        observeViewModel()

        initAdapter()
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



    fun uploadDriversList(secondaryDrivers : ArrayList<SecondaryDriver>){
        driversAdapter?.refreshAdapter(secondaryDrivers)
    }



    private fun initAdapter(){
        driversAdapter = SecondaryDriverAdapter(arrayListOf(), this)
        recyclerview_drivers.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = driversAdapter

        }

    }

    override fun onItemClick(position: Int) {


    }

    fun observeViewModel(){
        secondaryDriverViewModel.fetchError().observe(viewLifecycleOwner,
            Observer<String> {
                    t -> Toast.makeText(activity,t, Toast.LENGTH_LONG).show()
                if(t!=null){

                }
            })

        secondaryDriverViewModel.fetchInsertedId().observe(viewLifecycleOwner,
            Observer<Long> { t ->
                if(t != -1L){
                    Toast.makeText(activity,"Secondary Driver saved ", Toast.LENGTH_LONG).show()
                    activity?.let{
                        //launchSearchFragment()
                        bt_add.visibility=View.VISIBLE
                        bt_done.visibility = View.VISIBLE
                        bt_save.visibility = View.GONE
                        bt_cancel.visibility = View.GONE
                        drivers.add(getEnteredSecondaryDriverDetails())

                        uploadDriversList(drivers)
                        textViewadd.visibility = View.VISIBLE


                    }
                }else{
                    Toast.makeText(activity,"Secondary Driver not saved", Toast.LENGTH_LONG).show()

                }

            })
    }



    fun launchSearchFragment() {
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }


    fun launchRegisterFragment(resident: Resident) {
        activity?.replaceFragmentWithDataTest(ResidentRegisterFragment(), mContainerId,resident)
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
                if (profImage) {
                    img_profile.setBackgroundResource(0)
                    img_profile.setImageURI(Uri.fromFile(imgFile))
                    imgProfilePhotoPath = currentPhotoPath

                }

            }
        }

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {

            val imgFile = File(getRealPathFromURI(data?.data!!))
            if (imgFile.exists()) {
                if (profImage) {
                    img_profile.setBackgroundResource(0)
                    img_profile.setImageURI(data?.data)
                    imgProfilePhotoPath = getRealPathFromURI(data?.data!!)


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




    fun getEnteredSecondaryDriverDetails() : SecondaryDriver {
        val bmprofile = imgProfilePhotoPath
        return SecondaryDriver(
            0L,
            resident!!.carReg.trim(),
            resident!!.mobile!!.trim(),
            resident!!.address.trim(),
            et_name.text.toString(),
            et_surname.text.toString(), bmprofile!!
        )

    }



    override fun onDestroy() {
        super.onDestroy()

    }











}



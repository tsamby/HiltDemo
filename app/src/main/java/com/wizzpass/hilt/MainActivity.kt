package com.wizzpass.hilt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.wizzpass.hilt.data.local.db.entity.Supervisor
import com.wizzpass.hilt.ui.admin.AdminUserFragment
import com.wizzpass.hilt.ui.login.GuardLoginFragment
import com.wizzpass.hilt.ui.login.SupervisorViewModel
import com.wizzpass.hilt.util.replaceFragmentWithNoHistory
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7
    var doubleBackToExitPressedOnce = false
    private val supervisorViewModel : SupervisorViewModel by viewModels()
    var edt_pin : EditText? = null
    var dialogBuilder : android.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        checkAndroidVersion()



        replaceFragmentWithNoHistory(GuardLoginFragment(), R.id.container_fragment)
    }

    private fun checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions()
        } else {
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val wtite = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val read = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val sms = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        return true
    }

    //i need to figure out how to manage fragments
    override fun onBackPressed() {
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)*/
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_admin ->{
                showSupervisorDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    fun showSupervisorDialog() {

        dialogBuilder = android.app.AlertDialog.Builder(this@MainActivity).create()
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
        supervisorViewModel.supervisorFound.observe(this,
            Observer<Supervisor> {
                    t -> println("Received UserInfo List ${t}")
                if(t==null){
                    edt_pin!!.setError("Pin incorrect")
                }else{
                    dialogBuilder!!.dismiss()
                    replaceFragmentWithNoHistory(AdminUserFragment(), R.id.container_fragment)


                }


            }
        )
    }







}
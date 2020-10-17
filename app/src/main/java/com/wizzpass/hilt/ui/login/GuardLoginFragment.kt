package com.wizzpass.hilt.ui.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wizzpass.hilt.R
import com.wizzpass.hilt.data.local.db.entity.Guard
import com.wizzpass.hilt.data.local.db.entity.ResAddress
import com.wizzpass.hilt.data.local.db.entity.Supervisor
import com.wizzpass.hilt.ui.register.ResAddressViewModel
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.ui.search.SearchFragment
import com.wizzpass.hilt.util.replaceFragment
import com.wizzpass.hilt.util.setBorderRed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_guard_login.*
import kotlinx.android.synthetic.main.fragment_register_resident.*

/**
 * Created by novuyo on 20,September,2020
 */
@AndroidEntryPoint
class GuardLoginFragment : Fragment(), LifecycleOwner {

    private var guardView : View? = null
    var mContainerId:Int = -1
    private val guardLoginViewModel : GuardLoginViewModel by viewModels()
    private val resAddressViewModel : ResAddressViewModel by viewModels ()
    private val supervisorViewModel : SupervisorViewModel by viewModels ()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        guardView = inflater.inflate(R.layout.fragment_guard_login, container, false)
        mContainerId = container?.id?:-1
        return  guardView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.lifecycle.addObserver(guardLoginViewModel)

        setBorderRed(emergency)

        fetchDataFromViewModel()

        buttonLogin.setOnClickListener {
            guardLoginViewModel.fetchGuardByPasword(getEnteredSearchDetails())
            fetchGuardFromViewModel()
        }

        buttonEmergency.setOnClickListener {

            try {
                sendSmsMsgFnc("+27735603236","There is an emergency at the guard house!!!")
                sendSmsMsgFnc("+27824697314","There is an emergency at the guard house!!!")
            }catch (ex : java.lang.Exception){
                ex.printStackTrace()
            }



        }
    }

    fun getEnteredSearchDetails() : String {
        return et_password.text.toString()
    }


    fun getGuardDetailsDetails() : Guard {
        return Guard(
            0L,
            "admin",
            "0000"
        )
    }

    fun getSupervisorDetailsDetails() : Supervisor {
        return Supervisor(
            0L,
            "admin",
            "0000"
        )
    }


    fun launchSearchResidentFragment(){
        activity?.replaceFragment(SearchFragment(), mContainerId)
    }


    private fun fetchDataFromViewModel(){
        guardLoginViewModel.guardFinalList.observe(viewLifecycleOwner,
            Observer<MutableList<Guard>> {
                    t -> println("Received UserInfo List ${t.size}")
               if(t.size==0){
                   guardLoginViewModel.insertGuardInfo(getGuardDetailsDetails())
                   supervisorViewModel.insertSupervisorInfo(getSupervisorDetailsDetails())
                   resAddressViewModel.insertResAddressInfo()
               }

            }
        )
    }

    private fun fetchGuardFromViewModel(){
        guardLoginViewModel.guardFound.observe(viewLifecycleOwner,
            Observer<Guard> {
                    t -> println("Received UserInfo List ${t}")
                if(t==null){
                    et_password.setError("Password incorrect")
                }else{
                    launchSearchResidentFragment()
                }


            }
        )
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
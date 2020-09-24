package com.wizzpass.hilt

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.ui.login.GuardLoginFragment
import com.wizzpass.hilt.util.replaceFragmentWithNoHistory
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel : MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val addresses: ArrayList<ResAddress> = ArrayList<ResAddress>()
        addresses.add(ResAddress(0, "10 Bramley"))
        addresses.add(ResAddress(1, "11 Bramley"))
        addresses.add(ResAddress(2, "12 Bramley"))
        */
        fetchDataFromViewModel()
        replaceFragmentWithNoHistory(GuardLoginFragment(), R.id.container_fragment)
    }

    private fun fetchDataFromViewModel(){




    }
}
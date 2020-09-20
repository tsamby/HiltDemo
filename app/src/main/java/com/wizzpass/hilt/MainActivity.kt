package com.wizzpass.hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wizzpass.hilt.ui.login.GuardLoginFragment
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.util.replaceFragmentWithNoHistory
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragmentWithNoHistory(GuardLoginFragment(), R.id.container_fragment)
    }
}
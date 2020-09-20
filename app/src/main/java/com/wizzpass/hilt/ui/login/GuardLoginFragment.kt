package com.wizzpass.hilt.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.wizzpass.hilt.R
import com.wizzpass.hilt.ui.register.ResidentRegisterFragment
import com.wizzpass.hilt.util.replaceFragment
import kotlinx.android.synthetic.main.fragment_guard_login.*

/**
 * Created by novuyo on 20,September,2020
 */
class GuardLoginFragment : Fragment(), LifecycleOwner {

    private var guardView : View? = null
    var mContainerId:Int = -1
    private val guardLoginViewModel : GuardLoginViewModel by viewModels()
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
        buttonLogin.setOnClickListener {
            launchRegisterResidentFragment()
        }
    }

    fun launchRegisterResidentFragment(){
        activity?.replaceFragment(ResidentRegisterFragment(), mContainerId)
    }

}
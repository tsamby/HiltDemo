package com.wizzpass.hilt.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.data.local.db.entity.Guard
import com.wizzpass.hilt.data.local.db.repository.GuardDBRepository
import com.wizzpass.hilt.data.local.prefs.SharedPrefs
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 20,September,2020
 */
public class GuardLoginViewModel @ViewModelInject constructor(private val guardDBRepository: GuardDBRepository, val sharedPres : SharedPrefs) :
    ViewModel(), LifecycleObserver {

    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var guardFinalList: LiveData<MutableList<Guard>> = MutableLiveData<MutableList<Guard>>()
    var guardFound : LiveData<Guard> = MutableLiveData<Guard>()



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchAllGuards(){
        viewModelScope.launch {
            guardFinalList = guardDBRepository.fetchGuards()
        }
    }

    fun fetchGuardByPasword(password : String){
        viewModelScope.launch {
           guardFound = guardDBRepository.fetchGuardByPassword(password)

        }
    }

    fun insertGuardInfo(guard: Guard) {
        viewModelScope.launch {

            if(guard.userName.isNullOrEmpty() ||
                guard.password.isNullOrEmpty()
                 ){
                error.postValue( "Guard detail empty")
            }else{
                val guardId: Long = guardDBRepository.insertGuardData(guard)
                insertedId.postValue(guardId)
            }
        }
    }

    fun checkIfDbEmpty(){

    }


    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId


}
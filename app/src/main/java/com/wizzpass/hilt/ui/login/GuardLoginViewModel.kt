package com.wizzpass.hilt.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.repository.ResidentDBRepository
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 20,September,2020
 */
public class GuardLoginViewModel @ViewModelInject constructor(private val residentDBRepository: ResidentDBRepository) :
    ViewModel(), LifecycleObserver {

    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var residentFinalList: LiveData<MutableList<Resident>> = MutableLiveData<MutableList<Resident>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchResidentData(){
        viewModelScope.launch {
            residentFinalList = residentDBRepository.fetchResidents()
        }
    }

    fun insertResidentInfo(resident: Resident) {
        viewModelScope.launch {
            if(resident.fName.isNullOrEmpty() ||
                resident.lname.isNullOrEmpty() ||
                resident.carReg.isNullOrEmpty() ||
                resident.address.isNullOrEmpty() ){
                error.postValue( "Input Fields cannot be Empty")
            }else{
                val resId: Long = residentDBRepository.insertResidentData(resident)
                insertedId.postValue(resId)
            }
        }
    }

    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId


}
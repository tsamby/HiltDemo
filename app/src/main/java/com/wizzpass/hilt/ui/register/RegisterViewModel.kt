package com.wizzpass.hilt.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.data.local.db.entity.Resident
import com.wizzpass.hilt.data.local.db.repository.ResidentDBRepository
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 20,September,2020
 */
public class RegisterViewModel@ViewModelInject constructor(private val residentDBRepository: ResidentDBRepository) :
    ViewModel(), LifecycleObserver {


    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    private val  residentDetailsMissing  = MutableLiveData<String>()
    var residentFinalList:LiveData<MutableList<Resident>> = MutableLiveData<MutableList<Resident>>()
    var residentsLinkedToSameAddress : LiveData<MutableList<Resident>> = MutableLiveData<MutableList<Resident>>()
    var residentFound: LiveData<Resident> = MutableLiveData<Resident>()

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
                resident.address.isNullOrEmpty() ||
                resident.street_address.isNullOrEmpty()||
                resident.profImage.isNullOrEmpty()
            ){
                error.postValue( "Input Fields cannot be Empty")
            }else{
                val resId: Long = residentDBRepository.insertResidentData(resident)
                insertedId.postValue(resId)
            }
        }
    }


    fun checkResidentInfo(resident: Resident) {
        viewModelScope.launch {
            if(resident.fName.isNullOrEmpty() ||
                resident.lname.isNullOrEmpty() ||
                resident.address.isNullOrEmpty() ||
                resident.street_address.isNullOrEmpty()||
                resident.profImage.isNullOrEmpty()
            ){
                residentDetailsMissing.postValue( "Please enter all required fields")
            }else{
                residentDetailsMissing.postValue( "true")
            }
        }
    }

    fun fetchResidentByCarReg(searchField : String){
        viewModelScope.launch {
            residentFound = residentDBRepository.fetchResidentByCarReg(searchField)
        }
    }

    fun fetchResidentByMobile(searchField : String){
        viewModelScope.launch {
            residentFound = residentDBRepository.fetchResidentByMobile(searchField)
        }
    }

    fun fetchResidentByAddress(searchField : String){
        viewModelScope.launch {
            residentsLinkedToSameAddress = residentDBRepository.fetchResidentByAddress(searchField)
        }


    }

    fun fetchError(): LiveData<String> = error

    fun checkIfDetailsMissing(): LiveData<String> = residentDetailsMissing

    fun fetchInsertedId(): LiveData<Long> = insertedId

}
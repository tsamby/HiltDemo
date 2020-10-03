package com.wizzpass.hilt.ui.secondaryDrivers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.SecondaryDriver
import com.wizzpass.hilt.db.repository.SecondaryDriverRepository
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 03,October,2020
 */

public class SecondaryDriverViewModel@ViewModelInject constructor(private val secondaryDriverRepository: SecondaryDriverRepository) :
    ViewModel(), LifecycleObserver {


    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var secondaryDriverFinalList: LiveData<MutableList<SecondaryDriver>> = MutableLiveData<MutableList<SecondaryDriver>>()
    var secondaryDriversLinkedToSameAddress : LiveData<MutableList<SecondaryDriver>> = MutableLiveData<MutableList<SecondaryDriver>>()
    var secondaryDriverFound: LiveData<SecondaryDriver> = MutableLiveData<SecondaryDriver>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchSecondaryDriverData(){
        viewModelScope.launch {
            secondaryDriverFinalList = secondaryDriverRepository.fetchSecondaryDriverss()
        }
    }

    fun insertSecondaryDriverInfo(secondaryDriver: SecondaryDriver) {
        viewModelScope.launch {
            if(secondaryDriver.fName.isNullOrEmpty() ||
                secondaryDriver.lname.isNullOrEmpty() ||
                secondaryDriver.address.isNullOrEmpty() ||
                secondaryDriver.profImage.isNullOrEmpty()){
                error.postValue( "Input Fields cannot be Empty")
            }else{
                val resId: Long = secondaryDriverRepository.insertSecondaryDriverData(secondaryDriver)
                insertedId.postValue(resId)
            }
        }
    }

    fun fetchSecondaryDriverByCarReg(searchField : String){
        viewModelScope.launch {
            secondaryDriverFound = secondaryDriverRepository.fetchSecondaryDriversByCarReg(searchField)
        }
    }

    fun fetchSecondaryDriverByMobile(searchField : String){
        viewModelScope.launch {
            secondaryDriverFound = secondaryDriverRepository.fetchSecondaryDriversByMobile(searchField)
        }
    }

    fun fetchResidentByAddress(searchField : String){
        viewModelScope.launch {
            secondaryDriversLinkedToSameAddress = secondaryDriverRepository.fetchSecondaryByAddress(searchField)
        }


    }

    fun fetchResidentByMobile(searchField : String){
        viewModelScope.launch {
            secondaryDriversLinkedToSameAddress = secondaryDriverRepository.fetchSecondaryByMobile(searchField)
        }


    }

    fun fetchResidentByCarReg(searchField : String){
        viewModelScope.launch {
            secondaryDriversLinkedToSameAddress = secondaryDriverRepository.fetchSecondaryByCarReg(searchField)
        }


    }



    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId

}
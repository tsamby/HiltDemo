package com.wizzpass.hilt.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.repository.ResAddressDBRepository
import com.wizzpass.hilt.db.repository.ResidentDBRepository
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 20,September,2020
 */
public class ResAddressViewModel@ViewModelInject constructor(private val resAddressDBRepository: ResAddressDBRepository) :
    ViewModel(), LifecycleObserver {


    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var residentFinalList:LiveData<MutableList<ResAddress>> = MutableLiveData<MutableList<ResAddress>>()
    var residentsLinkedToSameAddress : LiveData<MutableList<ResAddress>> = MutableLiveData<MutableList<ResAddress>>()
    var residentFound: LiveData<Resident> = MutableLiveData<Resident>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchResidentData(){
        viewModelScope.launch {
            residentFinalList = resAddressDBRepository.fetchAddresses()
        }
    }

    fun insertResidentInfo(resAddress: ResAddress) {
        viewModelScope.launch {
            if(resAddress.resAddress.isNullOrEmpty()  ){
                error.postValue( "Address cannot be  Empty")
            }else{
                val resId: Long = resAddressDBRepository.insertResAddressData(resAddress)
                insertedId.postValue(resId)
            }
        }
    }

    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId

}
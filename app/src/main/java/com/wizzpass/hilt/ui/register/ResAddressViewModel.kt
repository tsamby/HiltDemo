package com.wizzpass.hilt.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.repository.ResAddressDBRepository
import com.wizzpass.hilt.db.repository.ResidentDBRepository
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 * Created by novuyo on 20,September,2020
 */
public class ResAddressViewModel@ViewModelInject constructor(private val resAddressDBRepository: ResAddressDBRepository) :
    ViewModel(), LifecycleObserver {


    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var residentFinalList:LiveData<MutableList<ResAddress>> = MutableLiveData<MutableList<ResAddress>>()
    var residentsLinkedToSameAddress : LiveData<MutableList<ResAddress>> = MutableLiveData<MutableList<ResAddress>>()
    var addressExists: LiveData<ResAddress> = MutableLiveData<ResAddress>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchAddressesData(){
        viewModelScope.launch {
            residentFinalList = resAddressDBRepository.fetchAddresses()
        }
    }

    fun insertAddressInfo(resAddress: ResAddress) {
        viewModelScope.launch {
            if(resAddress.resAddressNo.isNullOrEmpty()||
                resAddress.resAddressStreet.isNullOrEmpty() ){
                error.postValue( "Address cannot be  Empty")
            }else{
                val resId: Long = resAddressDBRepository.insertResAddressData(resAddress)
                insertedId.postValue(resId)
            }
        }
    }

    fun insertResAddressInfo() {
        viewModelScope.launch {
            val addresses: ArrayList<ResAddress> = ArrayList<ResAddress>()
            addresses.add(ResAddress(1, "10","Main Avenue"))
            addresses.add(ResAddress(2, "11","Pretoria Avenue"))
            addresses.add(ResAddress(3, "12","Dover Avenue"))
            addresses.add(ResAddress(4, "13","Hill Avenue"))
            addresses.add(ResAddress(5, "14","Crest Avenue"))
            addresses.add(ResAddress(6, "15","Denver Avenue"))
            resAddressDBRepository.insertResAddressDataList(addresses)
        }
    }

    fun cheeckIfAddressExists(address : String){
        viewModelScope.launch {
            addressExists = resAddressDBRepository.fetchAddresse(address)

        }
    }

    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId

}
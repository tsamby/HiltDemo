package com.wizzpass.hilt


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.ResAddress
import com.wizzpass.hilt.db.repository.ResAddressDBRepository
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 * Created by novuyo on 20,September,2020
 */
public class MainActivityViewModel @ViewModelInject constructor(private val resAddressDBRepository: ResAddressDBRepository) :
    ViewModel(), LifecycleObserver {

    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var addressFinalList: LiveData<MutableList<ResAddress>> = MutableLiveData<MutableList<ResAddress>>()
    var addressFound : LiveData<ResAddress> = MutableLiveData<ResAddress>()



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchAllAddresses(){
        viewModelScope.launch {
            addressFinalList = resAddressDBRepository.fetchAddresses()
        }
    }

    fun fetchAddress(searchField : String){
        viewModelScope.launch {
            addressFound = resAddressDBRepository.fetchAddresse(searchField)

        }
    }

    fun insertResInfo(resAddress: ResAddress) {
        viewModelScope.launch {

            val addresses: ArrayList<ResAddress> = ArrayList<ResAddress>()
            addresses.add(ResAddress(0, "10 Bramley"))
            addresses.add(ResAddress(1, "11 Bramley"))
            addresses.add(ResAddress(2, "12 Bramley"))
            resAddressDBRepository.insertResAddressDataList(addresses)




        }
    }

    fun checkIfDbEmpty(){

    }


    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId


}
package com.wizzpass.hilt.ui.additionalVehicles

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.data.local.db.entity.Vehicles
import com.wizzpass.hilt.data.local.db.repository.VehicleDBRepository
import kotlinx.coroutines.launch

public class AdditionalVehiclesViewModel@ViewModelInject constructor(private val vehicleDBRepository: VehicleDBRepository) :
    ViewModel(), LifecycleObserver {


    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var vehiclesFinalList: LiveData<MutableList<Vehicles>> = MutableLiveData<MutableList<Vehicles>>()
    var vehiclesLinkedToSameAddress : LiveData<MutableList<Vehicles>> = MutableLiveData<MutableList<Vehicles>>()
    var vehicleFound: LiveData<Vehicles> = MutableLiveData<Vehicles>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchVehiclesData(){
        viewModelScope.launch {
            vehiclesFinalList = vehicleDBRepository.fetchVehicles()
        }
    }

    fun insertVehicleInfo(vehicles: Vehicles) {
        viewModelScope.launch {
            if(vehicles.carReg.isNullOrEmpty() ||
                vehicles.address.isNullOrEmpty() ||
                vehicles.profImage.isNullOrEmpty()){
                error.postValue( "Input Fields cannot be Empty")
            }else{
                val resId: Long = vehicleDBRepository.insertVehicleData(vehicles)
                insertedId.postValue(resId)
            }
        }
    }

    fun fetchVehicleByCarReg(searchField : String){
        viewModelScope.launch {
            vehicleFound = vehicleDBRepository.fetchVehicleByCarReg(searchField)
        }
    }

    fun fetchVehicleByMobile(searchField : String){
        viewModelScope.launch {
            vehicleFound = vehicleDBRepository.fetchVehicleByMobile(searchField)
        }
    }

    fun fetchVehiclesByAddress(searchField : String){
        viewModelScope.launch {
            vehiclesLinkedToSameAddress = vehicleDBRepository.fetchVehiclesByAddress(searchField)
        }


    }

    fun fetchVehiclesByMobile(searchField : String){
        viewModelScope.launch {
            vehiclesLinkedToSameAddress = vehicleDBRepository.fetchVehiclesByMobile(searchField)
        }


    }

    fun fetchVehiclesByCarReg(searchField : String){
        viewModelScope.launch {
            vehiclesLinkedToSameAddress = vehicleDBRepository.fetchVehiclesByCarReg(searchField)
        }


    }



    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId

}
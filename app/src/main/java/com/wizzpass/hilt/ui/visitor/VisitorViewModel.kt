package com.wizzpass.hilt.ui.visitor

/**
 * Created by novuyo on 06,October,2020
 */

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.Visitor
import com.wizzpass.hilt.db.repository.ResidentDBRepository
import com.wizzpass.hilt.db.repository.VisitorDBRepository
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 20,September,2020
 */
public class VisitorViewModel@ViewModelInject constructor(private val visitorDBRepository: VisitorDBRepository) :
    ViewModel(), LifecycleObserver {


    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    private val  visitorDetailsMissing  = MutableLiveData<String>()
    var visitorFinalList:LiveData<MutableList<Visitor>> = MutableLiveData<MutableList<Visitor>>()
    var visitorsLinkedToSameAddress : LiveData<MutableList<Visitor>> = MutableLiveData<MutableList<Visitor>>()
    var visitorFound: LiveData<Visitor> = MutableLiveData<Visitor>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchVisitorData(){
        viewModelScope.launch {
            visitorFinalList = visitorDBRepository.fetchVisitors("")
        }
    }


    fun insertVisitorInfo(visitor: Visitor) {
        viewModelScope.launch {
            if(visitor.vis_fName.isNullOrEmpty() ||
                visitor.vis_lname.isNullOrEmpty() ||
                visitor.resAddress.isNullOrEmpty() ||
                visitor.res_street_address.isNullOrEmpty()||
                visitor.res_street_address.isNullOrEmpty()||
                visitor.reasonForVist.isNullOrEmpty()||
                visitor.vis_profImage.isNullOrEmpty()
            ){
                error.postValue( "Input Fields cannot be Empty")
            }else{
                val resId: Long = visitorDBRepository.insertVisitorData(visitor)
                insertedId.postValue(resId)
            }
        }
    }


    fun checkVisitorInfo(visitor: Visitor) {
        viewModelScope.launch {
            if(visitor.vis_fName.isNullOrEmpty() ||
                visitor.vis_lname.isNullOrEmpty() ||
                visitor.resAddress.isNullOrEmpty() ||
                visitor.res_street_address.isNullOrEmpty()||
                visitor.reasonForVist.isNullOrEmpty()||
                visitor.vis_profImage.isNullOrEmpty()
            ){
                visitorDetailsMissing.postValue( "Please enter all required fields")
            }else{
                visitorDetailsMissing.postValue( "true")
            }
        }
    }

    fun fetchVisitorByCarReg(searchField : String){
        viewModelScope.launch {
            visitorFound = visitorDBRepository.fetchVisitorByCarReg(searchField)
        }
    }

    fun fetchVisitorByMobile(searchField : String){
        viewModelScope.launch {
            visitorFound = visitorDBRepository.fetchVisitorByMobile(searchField)
        }
    }

    fun fetchVisitorByAddress(searchField : String){
        viewModelScope.launch {
            visitorsLinkedToSameAddress = visitorDBRepository.fetchVisitorByAddress(searchField)
        }


    }

    fun upDateExitTime(exitTime : String, visId : Long){
        viewModelScope.launch {
           visitorDBRepository.updateExitTimeStamp(exitTime,visId)
        }
    }

    fun fetchError(): LiveData<String> = error

    fun checkIfDetailsMissing(): LiveData<String> = visitorDetailsMissing

    fun fetchInsertedId(): LiveData<Long> = insertedId

}
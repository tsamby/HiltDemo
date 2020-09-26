package com.wizzpass.hilt.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.Supervisor
import com.wizzpass.hilt.db.repository.GuardDBRepository
import com.wizzpass.hilt.db.repository.SupervisorDBRepository
import kotlinx.coroutines.launch

/**
 * Created by novuyo on 26,September,2020
 */
public class SupervisorViewModel @ViewModelInject constructor(private val supervisorDBRepository: SupervisorDBRepository) :
    ViewModel(), LifecycleObserver {

    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    private val  numberOfSupervisors  = MutableLiveData<Int>()
    var supervisorFinalList: LiveData<MutableList<Supervisor>> = MutableLiveData<MutableList<Supervisor>>()
    var supervisorFound : LiveData<Supervisor> = MutableLiveData<Supervisor>()



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchAllSupervisors(){
        viewModelScope.launch {
            supervisorFinalList = supervisorDBRepository.fetchSupervisors()
        }
    }

    fun fetchSupervisorByPasword(password : String){
        viewModelScope.launch {
            supervisorFound = supervisorDBRepository.fetchSupervisorByPassword(password)

        }
    }

    fun insertSupervisorInfo(supervisor: Supervisor) {
        viewModelScope.launch {

            if(supervisor.userName.isNullOrEmpty() ||
                supervisor.password.isNullOrEmpty()
            ){
                error.postValue( "Supervisor detail empty")
            }else{
                val guardId: Long = supervisorDBRepository.insertSupervisorData(supervisor)
                insertedId.postValue(guardId)
            }
        }
    }



    fun fetchError(): LiveData<String> = error

    fun fetchInsertedId(): LiveData<Long> = insertedId


}
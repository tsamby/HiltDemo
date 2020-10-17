package com.wizzpass.hilt.data.local.db.repository

import com.wizzpass.hilt.data.local.db.dao.SupervisorDao
import com.wizzpass.hilt.data.local.db.entity.Supervisor
import javax.inject.Inject

/**
 * Created by novuyo on 26,September,2020
 */

class SupervisorDBRepository  @Inject constructor(private val supervisorDao: SupervisorDao){

    suspend fun  insertSupervisorData(supervisor: Supervisor) = supervisorDao.insert(supervisor)

    suspend fun  fetchSupervisors() = supervisorDao.fetch()

    suspend fun  fetchSupervisorByPassword(password : String) = supervisorDao.fetchSupervisorByPassword(password)

    suspend fun getSupervisorCount() = supervisorDao.getRowCount()
}
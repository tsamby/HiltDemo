package com.wizzpass.hilt.data.local.db.repository

import com.wizzpass.hilt.data.local.db.dao.ResidentDao
import com.wizzpass.hilt.data.local.db.entity.Resident
import javax.inject.Inject

/**
 * Created by novuyo on 20,September,2020
 */
class ResidentDBRepository  @Inject constructor(private val residentDao: ResidentDao){

    suspend fun  insertResidentData(resident: Resident) = residentDao.insert(resident)

    suspend fun  fetchResidents() = residentDao.fetch()

    suspend fun  fetchResidentByCarReg (searchField : String) = residentDao.fetchResidentByCarReg(searchField)

    suspend fun  fetchResidentByMobile (searchField : String) = residentDao.fetchResidentByMobile(searchField)

    suspend fun  fetchResidentByAddress (searchField : String) = residentDao.fetchAllResidentsLinkedToAddress(searchField)

    suspend fun deleteResidentData(resident: Resident) = residentDao.delete(resident)

    suspend fun updateResidentData(resident: Resident) = residentDao.update(resident)
}
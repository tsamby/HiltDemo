package com.wizzpass.hilt.db.repository

import com.wizzpass.hilt.db.dao.ResidentDao
import com.wizzpass.hilt.db.entity.Resident
import javax.inject.Inject

/**
 * Created by novuyo on 20,September,2020
 */
class ResidentDBRepository  @Inject constructor(private val residentDao: ResidentDao){

    suspend fun  insertResidentData(resident: Resident) = residentDao.insert(resident)

    suspend fun  fetchResidents() = residentDao.fetch()
}
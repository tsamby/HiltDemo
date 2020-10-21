package com.wizzpass.hilt.data.local.db.repository

import com.wizzpass.hilt.data.local.db.dao.GuardDao
import com.wizzpass.hilt.data.local.db.entity.Guard
import com.wizzpass.hilt.data.local.db.entity.Resident
import javax.inject.Inject

/**
 * Created by novuyo on 21,September,2020
 */

class GuardDBRepository  @Inject constructor(private val guardDao: GuardDao){

    suspend fun  insertGuardData(guard: Guard) = guardDao.insert(guard)

    suspend fun  fetchGuards() = guardDao.fetch()

    suspend fun  fetchGuardByPassword(password : String) = guardDao.fetchGuardByPassword(password)

    suspend fun getGuardCount() = guardDao.getRowCount()

    suspend fun updateGuardData(guard: Guard) = guardDao.update(guard)
}
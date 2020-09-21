package com.wizzpass.hilt.db.repository

import com.wizzpass.hilt.db.dao.GuardDao
import com.wizzpass.hilt.db.entity.Guard
import javax.inject.Inject

/**
 * Created by novuyo on 21,September,2020
 */

class GuardDBRepository  @Inject constructor(private val guardDao: GuardDao){

    suspend fun  insertGuardData(guard: Guard) = guardDao.insert(guard)

    suspend fun  fetchGuards() = guardDao.fetch()
}
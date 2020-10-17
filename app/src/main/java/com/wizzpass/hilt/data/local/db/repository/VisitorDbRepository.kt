package com.wizzpass.hilt.data.local.db.repository

import com.wizzpass.hilt.data.local.db.dao.VisitorDao
import com.wizzpass.hilt.data.local.db.entity.Visitor

import javax.inject.Inject

/**
 * Created by novuyo on 06,October,2020
 */
class VisitorDBRepository  @Inject constructor(private val visitorDao: VisitorDao){

    suspend fun  insertVisitorData(visitor: Visitor) = visitorDao.insert(visitor)

    suspend fun updateExitTimeStamp(exitTime : String, visId : Long) = visitorDao.updateExitTime(exitTime,visId)

    //suspend fun  fetchVisitors() = visitorDao.fetch()
    suspend fun  fetchVisitors(test : String) = visitorDao.fetch(test)

    suspend fun  fetchVisitorByCarReg (searchField : String) = visitorDao.fetchVisitorByCarReg(searchField)

    suspend fun  fetchVisitorByMobile (searchField : String) = visitorDao.fetchVisitorByMobile(searchField)

    suspend fun  fetchVisitorByAddress (searchField : String) = visitorDao.fetchAllVisitorsLinkedToAddress(searchField)
}
package com.wizzpass.hilt.db.repository

import com.wizzpass.hilt.db.dao.GuardDao
import com.wizzpass.hilt.db.dao.SecondaryDriverDao
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.SecondaryDriver
import javax.inject.Inject

/**
 * Created by novuyo on 03,October,2020
 */

class SecondaryDriverRepository  @Inject constructor(private val secondaryDriverDao: SecondaryDriverDao){

    suspend fun  insertSecondaryDriverData(secondaryDriver: SecondaryDriver) = secondaryDriverDao.insert(secondaryDriver)

    suspend fun  fetchSecondaryDriverss() = secondaryDriverDao.fetch()

    suspend fun  fetchSecondaryDriversByCarReg (searchField : String) = secondaryDriverDao.fetchSecondaryDriverCarReg(searchField)

    suspend fun  fetchSecondaryDriversByMobile (searchField : String) = secondaryDriverDao.fetchSecondaryDriverByMobile(searchField)

    suspend fun  fetchSecondaryByAddress (searchField : String) = secondaryDriverDao.fetchAllResidentsLinkedToAddress(searchField)

    suspend fun  fetchSecondaryByMobile (searchField : String) = secondaryDriverDao.fetchAllResidentsLinkedToMobile(searchField)

    suspend fun  fetchSecondaryByCarReg (searchField : String) = secondaryDriverDao.fetchAllResidentsLinkedToCarReg(searchField)


}
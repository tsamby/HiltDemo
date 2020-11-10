package com.wizzpass.hilt.data.local.db.repository

import com.wizzpass.hilt.data.local.db.dao.ResAddressDao
import com.wizzpass.hilt.data.local.db.entity.ResAddress
import com.wizzpass.hilt.data.local.db.entity.Supervisor
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by novuyo on 23,September,2020
 */

class ResAddressDBRepository  @Inject constructor(private val addressDao: ResAddressDao){

    suspend fun  insertResAddressData(resAddress: ResAddress) = addressDao.insert(resAddress)

    suspend fun  insertResAddressDataList(resAddress: ArrayList<ResAddress>) = addressDao.insertAll(resAddress)

    suspend fun  fetchAddresses() = addressDao.fetch()

    suspend fun  fetchAddresse(searchField : String) = addressDao.fetchAddress(searchField)

    suspend fun  fetchSpecificAddress(searchField : String) = addressDao.fetchSpecificAddress(searchField)

    suspend fun updateResAddress(resAddress: ResAddress) = addressDao.update(resAddress)


}
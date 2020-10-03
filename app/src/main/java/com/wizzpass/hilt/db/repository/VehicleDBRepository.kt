package com.wizzpass.hilt.db.repository

import com.wizzpass.hilt.db.dao.SecondaryDriverDao
import com.wizzpass.hilt.db.dao.SupervisorDao
import com.wizzpass.hilt.db.dao.VehiclesDao
import com.wizzpass.hilt.db.entity.SecondaryDriver
import com.wizzpass.hilt.db.entity.Supervisor
import com.wizzpass.hilt.db.entity.Vehicles
import javax.inject.Inject

class VehicleDBRepository  @Inject constructor(private val vehicleDao: VehiclesDao){

    suspend fun  insertVehicleData(vehicles: Vehicles) = vehicleDao.insert(vehicles)

    suspend fun  fetchVehicles() = vehicleDao.fetch()

    suspend fun  fetchVehicleByCarReg (searchField : String) = vehicleDao.fetchVehicleCarReg(searchField)

    suspend fun  fetchVehicleByMobile (searchField : String) = vehicleDao.fetchVehiclesByMobile(searchField)

    suspend fun  fetchVehiclesByAddress (searchField : String) = vehicleDao.fetchAllVehiclesLinkedToAddress(searchField)

    suspend fun  fetchVehiclesByMobile (searchField : String) = vehicleDao.fetchAllVehiclesLinkedToMobile(searchField)

    suspend fun  fetchVehiclesByCarReg (searchField : String) = vehicleDao.fetchAllVehiclesLinkedToCarReg(searchField)


}
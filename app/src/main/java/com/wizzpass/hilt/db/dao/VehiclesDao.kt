package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wizzpass.hilt.db.entity.SecondaryDriver
import com.wizzpass.hilt.db.entity.Vehicles

@Dao
interface  VehiclesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(vehicles: Vehicles) : Long

    @Query("select * From vehicles ORDER BY carReg ASC")
    fun  fetch() : LiveData<MutableList<Vehicles>>

    @Query("select * From vehicles WHERE carReg = :carReg LIMIT 1")
    fun  fetchVehicleCarReg(carReg : String) : LiveData<Vehicles>

    @Query("select * From vehicles WHERE mobile = :mobile LIMIT 1")
    fun  fetchVehiclesByMobile(mobile : String) : LiveData<Vehicles>

    @Query("select * From vehicles WHERE address = :address")
    fun  fetchAllVehiclesLinkedToAddress(address : String) : LiveData<MutableList<Vehicles>>

    @Query("select * From vehicles WHERE mobile = :mobile")
    fun  fetchAllVehiclesLinkedToMobile(mobile: String) : LiveData<MutableList<Vehicles>>

    @Query("select * From vehicles WHERE carReg = :carReg")
    fun  fetchAllVehiclesLinkedToCarReg(carReg : String) : LiveData<MutableList<Vehicles>>
}
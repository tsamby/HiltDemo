package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.SecondaryDriver

/**
 * Created by novuyo on 03,October,2020
 */
@Dao
interface  SecondaryDriverDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(secondaryDriver: SecondaryDriver) : Long

    @Query("select * From secondary_driver ORDER BY fName ASC")
    fun  fetch() : LiveData<MutableList<SecondaryDriver>>

    @Query("select * From secondary_driver WHERE carReg = :carReg LIMIT 1")
    fun  fetchSecondaryDriverCarReg(carReg : String) : LiveData<SecondaryDriver>

    @Query("select * From secondary_driver WHERE mobile = :mobile LIMIT 1")
    fun  fetchSecondaryDriverByMobile(mobile : String) : LiveData<SecondaryDriver>

    @Query("select * From secondary_driver WHERE address = :address")
    fun  fetchAllResidentsLinkedToAddress(address : String) : LiveData<MutableList<SecondaryDriver>>

    @Query("select * From secondary_driver WHERE mobile = :mobile")
    fun  fetchAllResidentsLinkedToMobile(mobile: String) : LiveData<MutableList<SecondaryDriver>>

    @Query("select * From secondary_driver WHERE carReg = :carReg")
    fun  fetchAllResidentsLinkedToCarReg(carReg : String) : LiveData<MutableList<SecondaryDriver>>
}
package com.wizzpass.hilt.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wizzpass.hilt.data.local.db.entity.Resident
import com.wizzpass.hilt.data.local.db.entity.SecondaryDriver


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
    fun  fetchAllSecondaryDriverLinkedToAddress(address : String) : LiveData<MutableList<SecondaryDriver>>

    @Query("select * From secondary_driver WHERE mobile = :mobile")
    fun  fetchAllSecondaryDriverLinkedToMobile(mobile: String) : LiveData<MutableList<SecondaryDriver>>

    @Query("select * From secondary_driver WHERE carReg = :carReg")
    fun  fetchAllSecondaryDriverLinkedToCarReg(carReg : String) : LiveData<MutableList<SecondaryDriver>>

    @Delete
    suspend fun delete(secondaryDriver: SecondaryDriver) : Int

}
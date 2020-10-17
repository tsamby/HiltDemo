package com.wizzpass.hilt.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wizzpass.hilt.data.local.db.entity.Resident


/**
 * Created by novuyo on 20,September,2020
 */
@Dao
interface  ResidentDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(resident : Resident) : Long

    @Query("select * From resident ORDER BY carReg ASC")
    fun  fetch() : LiveData<MutableList<Resident>>

    @Query("select * From resident WHERE carReg = :carReg LIMIT 1")
    fun  fetchResidentByCarReg(carReg : String) : LiveData<Resident>

    @Query("select * From resident WHERE mobile = :mobile LIMIT 1")
    fun  fetchResidentByMobile(mobile : String) : LiveData<Resident>

    @Query("select * From resident WHERE address = :address")
    fun  fetchAllResidentsLinkedToAddress(address : String) : LiveData<MutableList<Resident>>

    @Query(" DELETE FROM resident WHERE resId = :resId")
    suspend fun delete(resId : Long)

    @Delete
    suspend fun delete(resident: Resident) : Int

    @Update
    suspend fun update(resident: Resident)

}
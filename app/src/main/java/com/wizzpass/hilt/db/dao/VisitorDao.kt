package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wizzpass.hilt.db.entity.Visitor

/**
 * Created by novuyo on 06,October,2020
 */


@Dao
interface  VisitorDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(visitor: Visitor) : Long

    @Query("select * From visitor ORDER BY visCarReg  ASC")
    fun  fetch() : LiveData<MutableList<Visitor>>

    @Query("select * From visitor WHERE visCarReg = :carReg LIMIT 1")
    fun  fetchVisitorByCarReg(carReg : String) : LiveData<Visitor>

    @Query("select * From visitor WHERE visMobile = :mobile LIMIT 1")
    fun  fetchVisitorByMobile(mobile : String) : LiveData<Visitor>

    @Query("select * From visitor WHERE resAddress = :address")
    fun  fetchAllVisitorsLinkedToAddress(address : String) : LiveData<MutableList<Visitor>>
}
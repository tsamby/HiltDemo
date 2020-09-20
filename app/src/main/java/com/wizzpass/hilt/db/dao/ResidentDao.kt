package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wizzpass.hilt.db.entity.Resident

/**
 * Created by novuyo on 20,September,2020
 */
@Dao
interface  ResidentDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(resident : Resident) : Long

    @Query("select * From resident ORDER BY carReg ASC")
    fun  fetch() : LiveData<MutableList<Resident>>

}
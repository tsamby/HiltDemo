package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wizzpass.hilt.db.entity.Guard


/**
 * Created by novuyo on 20,September,2020
 */
@Dao
interface GuardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(guard: Guard) : Long

    @Query("select * From guard ORDER BY userName ASC")
    fun  fetch() : LiveData<MutableList<Guard>>
}
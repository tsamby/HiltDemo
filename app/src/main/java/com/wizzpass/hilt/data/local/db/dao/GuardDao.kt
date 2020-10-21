package com.wizzpass.hilt.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wizzpass.hilt.data.local.db.entity.Guard
import com.wizzpass.hilt.data.local.db.entity.Resident


/**
 * Created by novuyo on 20,September,2020
 */
@Dao
interface GuardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(guard: Guard) : Long

    @Query("select * From guard ORDER BY userName ASC")
    fun  fetch() : LiveData<MutableList<Guard>>

    @Query("select * From guard WHERE password = :password")
    fun  fetchGuardByPassword(password : String) : LiveData<Guard>

    @Query("DELETE FROM guard")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM guard")
    fun getRowCount(): LiveData<Int>?

    @Update
    suspend fun update(guard: Guard)

}
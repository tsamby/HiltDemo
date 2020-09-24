package com.wizzpass.hilt.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wizzpass.hilt.db.entity.Guard
import com.wizzpass.hilt.db.entity.ResAddress
import io.reactivex.Single


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



}
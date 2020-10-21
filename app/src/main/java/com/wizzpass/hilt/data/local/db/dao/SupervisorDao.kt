package com.wizzpass.hilt.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wizzpass.hilt.data.local.db.entity.Guard
import com.wizzpass.hilt.data.local.db.entity.Supervisor


/**
 * Created by novuyo on 20,September,2020
 */

@Dao
interface SupervisorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(supervisor: Supervisor) : Long

    @Query("select * From supervisor ORDER BY userName ASC")
    fun  fetch() : LiveData<MutableList<Supervisor>>

    @Query("select * From supervisor WHERE password = :password")
    fun  fetchSupervisorByPassword(password : String) : LiveData<Supervisor>

    @Query("DELETE FROM supervisor")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM guard")
    fun getRowCount(): LiveData<Int>?

    @Update
    suspend fun update(supervisor: Supervisor)

}